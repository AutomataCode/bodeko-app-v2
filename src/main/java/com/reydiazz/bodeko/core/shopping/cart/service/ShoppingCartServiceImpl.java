package com.reydiazz.bodeko.core.shopping.cart.service;

import com.github.f4b6a3.uuid.UuidCreator;
import com.reydiazz.bodeko.core.cart.items.model.entity.ShoppingCartItem;
import com.reydiazz.bodeko.core.cart.items.repository.ShoppingCartItemRepository;
import com.reydiazz.bodeko.core.product.core.model.entity.Product;
import com.reydiazz.bodeko.core.product.core.model.enums.ProductStatus;
import com.reydiazz.bodeko.core.product.core.service.ProductService;
import com.reydiazz.bodeko.core.product.offer.service.ProductOfferService;
import com.reydiazz.bodeko.core.shopping.cart.component.ShoppingCartMapper;
import com.reydiazz.bodeko.core.shopping.cart.exception.ShoppingCartItemNotFoundException;
import com.reydiazz.bodeko.core.shopping.cart.exception.ShoppingCartNotFoundException;
import com.reydiazz.bodeko.core.shopping.cart.exception.ShoppingCartProductNotAvailableException;
import com.reydiazz.bodeko.core.shopping.cart.model.entity.ShoppingCart;
import com.reydiazz.bodeko.core.shopping.cart.model.enums.ShoppingCartStatus;
import com.reydiazz.bodeko.core.shopping.cart.repository.ShoppingCartRepository;
import com.reydiazz.bodeko.core.shopping.cart.web.request.AddCartItemRequest;
import com.reydiazz.bodeko.core.shopping.cart.web.request.EditCartItemQuantityRequest;
import com.reydiazz.bodeko.core.shopping.cart.web.response.ShoppingCartResponse;
import com.reydiazz.bodeko.core.store.service.StoreService;
import com.reydiazz.bodeko.core.user.core.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private static final int DEFAULT_QUANTITY = 1;

    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartItemRepository shoppingCartItemRepository;
    private final ShoppingCartMapper shoppingCartMapper;

    private final StoreService storeService;
    private final UserService userService;
    private final ProductService productService;
    private final ProductOfferService productOfferService;

    @Override
    @Transactional(readOnly = true)
    public ShoppingCartResponse getCart(UUID userId, UUID storeId) {
        storeService.findEntityById(storeId);
        Optional<ShoppingCart> cart = findOpenCart(userId, storeId);
        return buildResponse(cart.orElse(null), storeId);
    }

    @Override
    @Transactional
    public ShoppingCartResponse addItem(UUID userId, UUID storeId, AddCartItemRequest request) {
        storeService.findEntityById(storeId);
        Product product = productService.findEntityById(request.productId(), storeId);
        verifyAvailable(product);
        ShoppingCart cart = findOpenCart(userId, storeId).orElseGet(() -> createCart(userId, storeId));
        int quantity = request.quantity() != null ? request.quantity() : DEFAULT_QUANTITY;
        Optional<ShoppingCartItem> existing = shoppingCartItemRepository.findByCartIdAndProductId(cart.getId(), product.getId());
        if (existing.isPresent()) {
            existing.get().increaseQuantity(quantity);
        } else {
            ShoppingCartItem item = ShoppingCartItem.builder()
                    .cartId(cart.getId())
                    .productId(product.getId())
                    .storeId(storeId)
                    .cart(cart)
                    .product(product)
                    .quantity(quantity)
                    .build();
            shoppingCartItemRepository.save(item);
        }
        return buildResponse(cart, storeId);
    }

    @Override
    @Transactional
    public ShoppingCartResponse updateItemQuantity(
            UUID userId,
            UUID storeId,
            UUID productId,
            EditCartItemQuantityRequest request
    ) {
        ShoppingCart cart = findOpenCartOrThrow(userId, storeId);
        ShoppingCartItem item = findItemOrThrow(cart.getId(), productId);
        item.updateQuantity(request.quantity());
        return buildResponse(cart, storeId);
    }

    @Override
    @Transactional
    public ShoppingCartResponse removeItem(UUID userId, UUID storeId, UUID productId) {
        ShoppingCart cart = findOpenCartOrThrow(userId, storeId);
        ShoppingCartItem item = findItemOrThrow(cart.getId(), productId);
        shoppingCartItemRepository.delete(item);
        return buildResponse(cart, storeId);
    }

    @Override
    @Transactional
    public ShoppingCart findOpenCartForCheckout(UUID userId, UUID storeId) {
        return shoppingCartRepository.findByUserIdAndStoreIdAndStatusForUpdate(userId, storeId, ShoppingCartStatus.OPEN)
                .orElseThrow(ShoppingCartNotFoundException::new);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShoppingCartItem> findItems(UUID cartId) {
        return shoppingCartItemRepository.findAllWithProductByCartId(cartId);
    }

    @Override
    @Transactional
    public void markAsConverted(ShoppingCart cart) {
        cart.markAsConverted();
    }

    private Optional<ShoppingCart> findOpenCart(UUID userId, UUID storeId) {
        return shoppingCartRepository.findByUserIdAndStoreIdAndStatus(userId, storeId, ShoppingCartStatus.OPEN);
    }

    private ShoppingCart findOpenCartOrThrow(UUID userId, UUID storeId) {
        return findOpenCart(userId, storeId).orElseThrow(ShoppingCartNotFoundException::new);
    }

    private ShoppingCartItem findItemOrThrow(UUID cartId, UUID productId) {
        return shoppingCartItemRepository.findByCartIdAndProductId(cartId, productId)
                .orElseThrow(ShoppingCartItemNotFoundException::new);
    }

    private ShoppingCart createCart(UUID userId, UUID storeId) {
        ShoppingCart cart = ShoppingCart.builder()
                .id(UuidCreator.getTimeOrderedEpoch())
                .user(userService.getReferenceById(userId))
                .store(storeService.getReferenceById(storeId))
                .createdAt(OffsetDateTime.now())
                .build();
        return shoppingCartRepository.save(cart);
    }

    private void verifyAvailable(Product product) {
        if (product.getStatus() != ProductStatus.ACTIVE) {
            throw new ShoppingCartProductNotAvailableException();
        }
    }

    private ShoppingCartResponse buildResponse(ShoppingCart cart, UUID storeId) {
        if (cart == null) {
            return shoppingCartMapper.toResponse(null, storeId, List.of(), Map.of());
        }
        List<ShoppingCartItem> items = shoppingCartItemRepository.findAllWithProductByCartId(cart.getId());
        List<UUID> productIds = items.stream().map(ShoppingCartItem::getProductId).toList();
        Map<UUID, BigDecimal> offerPrices = productOfferService.findActiveOfferPrices(productIds, OffsetDateTime.now());
        return shoppingCartMapper.toResponse(cart, storeId, items, offerPrices);
    }

}
