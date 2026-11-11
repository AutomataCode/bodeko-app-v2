package com.reydiazz.bodeko.core.order.core.service;

import com.github.f4b6a3.uuid.UuidCreator;
import com.reydiazz.bodeko.core.cart.items.model.entity.ShoppingCartItem;
import com.reydiazz.bodeko.core.order.core.component.OrderMapper;
import com.reydiazz.bodeko.core.order.core.exception.OrderEmptyCartException;
import com.reydiazz.bodeko.core.order.core.exception.OrderInvalidDocumentNumberException;
import com.reydiazz.bodeko.core.order.core.exception.OrderInvoiceRequiresBusinessNameException;
import com.reydiazz.bodeko.core.order.core.exception.OrderInvoiceRequiresRucException;
import com.reydiazz.bodeko.core.order.core.exception.OrderProductNotAvailableException;
import com.reydiazz.bodeko.core.order.core.exception.OrderReceiptRequiresDniException;
import com.reydiazz.bodeko.core.order.core.model.entity.Order;
import com.reydiazz.bodeko.core.order.core.model.enums.CustomerDocumentType;
import com.reydiazz.bodeko.core.order.core.model.enums.InvoiceType;
import com.reydiazz.bodeko.core.order.core.repository.OrderRepository;
import com.reydiazz.bodeko.core.order.core.web.request.CheckoutRequest;
import com.reydiazz.bodeko.core.order.core.web.response.OrderResponse;
import com.reydiazz.bodeko.core.inventory.service.InventoryService;
import com.reydiazz.bodeko.core.order.items.model.entity.OrderItem;
import com.reydiazz.bodeko.core.order.items.repository.OrderItemRepository;
import com.reydiazz.bodeko.core.product.core.model.entity.Product;
import com.reydiazz.bodeko.core.product.core.model.enums.ProductStatus;
import com.reydiazz.bodeko.core.product.offer.service.ProductOfferService;
import com.reydiazz.bodeko.core.shopping.cart.model.entity.ShoppingCart;
import com.reydiazz.bodeko.core.shopping.cart.service.ShoppingCartService;
import com.reydiazz.bodeko.core.store.model.entity.Store;
import com.reydiazz.bodeko.core.store.service.StoreService;
import com.reydiazz.bodeko.core.user.core.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private static final BigDecimal ZERO = BigDecimal.ZERO.setScale(2);
    private static final String DNI_PATTERN = "^\\d{8}$";
    private static final String RUC_PATTERN = "^\\d{11}$";
    private static final DateTimeFormatter ORDER_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final int ORDER_SUFFIX_LENGTH = 8;

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderMapper orderMapper;

    private final StoreService storeService;
    private final UserService userService;
    private final ShoppingCartService shoppingCartService;
    private final ProductOfferService productOfferService;
    private final InventoryService inventoryService;
    private final com.reydiazz.bodeko.core.product.core.repository.ProductRepository productRepository;
    private final jakarta.persistence.EntityManager entityManager;

    /** Consulta una orden que pertenezca al cliente y a la tienda indicados. */
    @Override
    @Transactional(readOnly = true)
    public OrderResponse getByCustomer(UUID userId, UUID storeId, UUID orderId) {
        Order order = orderRepository.findByIdAndStoreIdAndCustomerId(orderId, storeId, userId)
                .orElseThrow(com.reydiazz.bodeko.core.order.management.exception.OrderManagementNotFoundException::new);
        return orderMapper.toResponse(order, orderItemRepository.findAllByOrderIdOrderByProductNameAsc(orderId));
    }

    @Override
    @Transactional
    public OrderResponse checkout(UUID userId, UUID storeId, CheckoutRequest request) {
        Store store = storeService.findEntityById(storeId);
        boolean isInvoice = request.invoiceType() == InvoiceType.INVOICE;
        String documentNumber = request.documentNumber().trim();
        String businessName = sanitize(request.businessName());
        validateFiscalData(request, documentNumber, businessName);

        ShoppingCart cart = shoppingCartService.findOpenCartForCheckout(userId, storeId);
        List<ShoppingCartItem> cartItems = shoppingCartService.findItems(cart.getId());
        if (cartItems.isEmpty()) {
            throw new OrderEmptyCartException();
        }

        // Todos los procesos bloquean productos en el mismo orden para reducir interbloqueos.
        cartItems.stream().map(ShoppingCartItem::getProductId).sorted().forEach(id -> {
            Product locked = productRepository.findByIdAndStoreIdForUpdate(id, storeId)
                .orElseThrow(com.reydiazz.bodeko.core.product.core.exception.ProductNotFoundException::new);
            entityManager.refresh(locked);
            if (locked.getStatus() != ProductStatus.ACTIVE) throw new OrderProductNotAvailableException(locked.getName());
        });
        OffsetDateTime now = OffsetDateTime.now();
        List<UUID> productIds = cartItems.stream().map(ShoppingCartItem::getProductId).toList();
        Map<UUID, BigDecimal> offerPrices = productOfferService.findActiveOfferPrices(productIds, now);
        List<OrderLine> lines = cartItems.stream()
                .sorted(Comparator.comparing(ShoppingCartItem::getProductId))
                .map(item -> toLine(item, offerPrices))
                .toList();

        BigDecimal subtotal = sum(lines.stream().map(OrderLine::itemSubtotal));
        BigDecimal taxableBase = sum(lines.stream()
                .filter(line -> !line.product().isTaxExempt())
                .map(OrderLine::itemSubtotal));
        BigDecimal taxAmount = taxableBase.multiply(store.getTaxRate()).setScale(2, RoundingMode.HALF_UP);
        BigDecimal total = subtotal.add(taxAmount);
        if (total.signum()<=0 || total.movePointRight(2).compareTo(BigDecimal.valueOf(Integer.MAX_VALUE))>0) {
            throw new com.reydiazz.bodeko.shared.exception.BusinessException(com.reydiazz.bodeko.shared.validation.ValidationError.PRICE_LIMIT);
        }

        lines.forEach(line -> inventoryService.decreaseStock(line.product().getId(), line.cartItem().getQuantity()));

        Order order = Order.builder()
                .id(UuidCreator.getTimeOrderedEpoch())
                .store(store)
                .customer(userService.getReferenceById(userId))
                .orderNumber(generateOrderNumber(now))
                .invoiceType(request.invoiceType())
                .customerDocumentType(request.documentType())
                .customerDocumentNumber(documentNumber)
                .customerBusinessName(isInvoice ? businessName : null)
                .customerBillingAddress(isInvoice ? sanitize(request.billingAddress()) : null)
                .subtotal(subtotal)
                .taxAmount(taxAmount)
                .total(total)
                .appliedTaxRate(store.getTaxRate())
                .createdAt(now)
                .build();
        Order orderSaved = orderRepository.save(order);

        List<OrderItem> orderItems = lines.stream()
                .map(line -> toOrderItem(orderSaved, line))
                .toList();
        orderItemRepository.saveAll(orderItems);

        shoppingCartService.markAsConverted(cart);
        return orderMapper.toResponse(orderSaved, orderItems);
    }

    private void validateFiscalData(CheckoutRequest request, String documentNumber, String businessName) {
        if (request.invoiceType() == InvoiceType.INVOICE) {
            if (request.documentType() != CustomerDocumentType.RUC) {
                throw new OrderInvoiceRequiresRucException();
            }
            if (businessName == null) {
                throw new OrderInvoiceRequiresBusinessNameException();
            }
        } else if (request.documentType() != CustomerDocumentType.DNI) {
            throw new OrderReceiptRequiresDniException();
        }
        String pattern = request.documentType() == CustomerDocumentType.RUC ? RUC_PATTERN : DNI_PATTERN;
        if (!documentNumber.matches(pattern)) {
            throw new OrderInvalidDocumentNumberException();
        }
    }

    private OrderLine toLine(ShoppingCartItem item, Map<UUID, BigDecimal> offerPrices) {
        Product product = item.getProduct();
        if (product.getStatus() != ProductStatus.ACTIVE) {
            throw new OrderProductNotAvailableException(product.getName());
        }
        BigDecimal chargedUnitPrice = offerPrices.getOrDefault(product.getId(), product.getBasePrice());
        BigDecimal itemSubtotal = chargedUnitPrice.multiply(BigDecimal.valueOf(item.getQuantity()));
        return new OrderLine(item, product, product.getBasePrice(), chargedUnitPrice, itemSubtotal);
    }

    private OrderItem toOrderItem(Order order, OrderLine line) {
        return OrderItem.builder()
                .orderId(order.getId())
                .productId(line.product().getId())
                .storeId(order.getStore().getId())
                .order(order)
                .product(line.product())
                .productName(line.product().getName())
                .quantity(line.cartItem().getQuantity())
                .listUnitPrice(line.listUnitPrice())
                .chargedUnitPrice(line.chargedUnitPrice())
                .itemSubtotal(line.itemSubtotal())
                .build();
    }

    private BigDecimal sum(Stream<BigDecimal> amounts) {
        return amounts.reduce(ZERO, BigDecimal::add);
    }

    private String generateOrderNumber(OffsetDateTime moment) {
        String randomPart = UuidCreator.getTimeOrderedEpoch().toString().replace("-", "");
        String suffix = randomPart.substring(randomPart.length() - ORDER_SUFFIX_LENGTH).toUpperCase();
        return "BOD-" + moment.withOffsetSameInstant(ZoneOffset.UTC).format(ORDER_DATE_FORMAT) + "-" + suffix;
    }

    private String sanitize(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private record OrderLine(
            ShoppingCartItem cartItem,
            Product product,
            BigDecimal listUnitPrice,
            BigDecimal chargedUnitPrice,
            BigDecimal itemSubtotal
    ) {
    }

}
