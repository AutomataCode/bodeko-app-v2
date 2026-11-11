package com.reydiazz.bodeko.core.order.management.service;

import com.github.f4b6a3.uuid.UuidCreator;
import com.reydiazz.bodeko.core.inventory.service.InventoryService;
import com.reydiazz.bodeko.core.order.core.component.OrderMapper;
import com.reydiazz.bodeko.core.order.core.model.entity.Order;
import com.reydiazz.bodeko.core.order.core.model.enums.OrderStatus;
import com.reydiazz.bodeko.core.order.core.repository.OrderRepository;
import com.reydiazz.bodeko.core.order.core.web.response.OrderResponse;
import com.reydiazz.bodeko.core.order.history.model.entity.OrderStatusHistory;
import com.reydiazz.bodeko.core.order.history.repository.OrderStatusHistoryRepository;
import com.reydiazz.bodeko.core.order.items.model.entity.OrderItem;
import com.reydiazz.bodeko.core.order.items.repository.OrderItemRepository;
import com.reydiazz.bodeko.core.order.management.component.OrderManagementMapper;
import com.reydiazz.bodeko.core.order.management.exception.OrderManagementInvalidTransitionException;
import com.reydiazz.bodeko.core.order.management.exception.OrderManagementNotFoundException;
import com.reydiazz.bodeko.core.order.management.web.request.ChangeOrderStatusRequest;
import com.reydiazz.bodeko.core.order.management.web.response.OrderSummaryResponse;
import com.reydiazz.bodeko.core.payment.service.PaymentService;
import com.reydiazz.bodeko.shared.response.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderManagementServiceImpl implements OrderManagementService {

    private static final int MAX_PAGE_SIZE = 100;

    private static final Map<OrderStatus, Set<OrderStatus>> ALLOWED_TRANSITIONS = Map.of(
            OrderStatus.PENDING, Set.of(OrderStatus.CANCELLED),
            OrderStatus.PAID, Set.of(OrderStatus.SHIPPED, OrderStatus.CANCELLED),
            OrderStatus.SHIPPED, Set.of(OrderStatus.DELIVERED),
            OrderStatus.DELIVERED, Set.of(),
            OrderStatus.CANCELLED, Set.of()
    );

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderStatusHistoryRepository orderStatusHistoryRepository;
    private final OrderMapper orderMapper;
    private final OrderManagementMapper orderManagementMapper;

    private final InventoryService inventoryService;
    private final PaymentService paymentService;
    private final jakarta.persistence.EntityManager entityManager;

    @Override
    @Transactional(readOnly = true)
    public PageResponse<OrderSummaryResponse> getAll(UUID storeId, OrderStatus status, int page, int size) {
        Pageable pageable = buildPageable(page, size);
        Page<Order> orders = status == null
                ? orderRepository.findAllByStoreId(storeId, pageable)
                : orderRepository.findAllByStoreIdAndStatus(storeId, status, pageable);
        return new PageResponse<>(
                orders.getContent().stream().map(orderManagementMapper::toSummary).toList(),
                orders.getNumber(),
                orders.getSize(),
                orders.getTotalElements(),
                orders.getTotalPages()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getById(UUID orderId, UUID storeId) {
        Order order = orderRepository.findByIdAndStoreId(orderId, storeId)
                .orElseThrow(OrderManagementNotFoundException::new);
        List<OrderItem> items = orderItemRepository.findAllByOrderIdOrderByProductNameAsc(order.getId());
        return orderMapper.toResponse(order, items);
    }

    @Override
    @Transactional
    public OrderResponse changeStatus(UUID orderId, UUID storeId, UUID adminId, ChangeOrderStatusRequest request) {
        Order order = orderRepository.findByIdAndStoreIdForUpdate(orderId, storeId)
                .orElseThrow(OrderManagementNotFoundException::new);
        // El estado se relee dentro del bloqueo, también después de una espera.
        entityManager.refresh(order);
        OrderStatus from = order.getStatus();
        OrderStatus to = request.status();
        if (!ALLOWED_TRANSITIONS.get(from).contains(to)) {
            throw new OrderManagementInvalidTransitionException(from, to);
        }

        List<OrderItem> items = orderItemRepository.findAllByOrderIdOrderByProductNameAsc(order.getId());
        if (to == OrderStatus.CANCELLED) {
            restoreStock(items);
        }
        order.changeStatus(to);
        recordTransition(order.getId(), from, to, adminId);

        if (to == OrderStatus.CANCELLED && from == OrderStatus.PAID) {
            paymentService.refundApprovedPayment(order.getId());
        }
        return orderMapper.toResponse(order, items);
    }

    private void restoreStock(List<OrderItem> items) {
        items.stream()
                .sorted(Comparator.comparing(OrderItem::getProductId))
                .forEach(item -> inventoryService.increaseStock(item.getProductId(), item.getQuantity()));
    }

    private void recordTransition(UUID orderId, OrderStatus from, OrderStatus to, UUID adminId) {
        OrderStatusHistory history = OrderStatusHistory.builder()
                .id(UuidCreator.getTimeOrderedEpoch())
                .orderId(orderId)
                .fromStatus(from)
                .toStatus(to)
                .changedBy(adminId)
                .createdAt(OffsetDateTime.now())
                .build();
        orderStatusHistoryRepository.saveAndFlush(history);
    }

    private Pageable buildPageable(int page, int size) {
        int safePage = Math.max(page, 0);
        int safeSize = Math.min(Math.max(size, 1), MAX_PAGE_SIZE);
        return PageRequest.of(safePage, safeSize, Sort.by(Sort.Direction.DESC, "createdAt"));
    }

}
