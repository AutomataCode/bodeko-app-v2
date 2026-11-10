package com.reydiazz.bodeko.core.order.customer.service;

import com.reydiazz.bodeko.core.order.core.component.OrderMapper;
import com.reydiazz.bodeko.core.order.core.model.entity.Order;
import com.reydiazz.bodeko.core.order.core.model.enums.OrderStatus;
import com.reydiazz.bodeko.core.order.core.repository.OrderRepository;
import com.reydiazz.bodeko.core.order.core.web.response.OrderResponse;
import com.reydiazz.bodeko.core.order.customer.web.response.CustomerOrderDetailResponse;
import com.reydiazz.bodeko.core.order.customer.web.response.CustomerOrderSummaryResponse;
import com.reydiazz.bodeko.core.order.items.repository.OrderItemRepository;
import com.reydiazz.bodeko.core.order.management.exception.OrderManagementNotFoundException;
import com.reydiazz.bodeko.core.order.management.service.OrderManagementService;
import com.reydiazz.bodeko.core.order.management.web.request.ChangeOrderStatusRequest;
import com.reydiazz.bodeko.shared.response.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

/** Reutiliza la cancelación existente para devolver stock, reembolsar y registrar el cambio. */
@Service
@RequiredArgsConstructor
public class CustomerOrderServiceImpl implements CustomerOrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderMapper orderMapper;
    private final OrderManagementService orderManagementService;

    @Override
    @Transactional(readOnly = true)
    public PageResponse<CustomerOrderSummaryResponse> list(UUID customerId, OrderStatus status, int page, int size) {
        PageRequest pageable = PageRequest.of(Math.max(0, page), Math.min(100, Math.max(1, size)),
                Sort.by(Sort.Direction.DESC, "createdAt", "id"));
        Page<Order> orders = status == null
                ? orderRepository.findAllByCustomerId(customerId, pageable)
                : orderRepository.findAllByCustomerIdAndStatus(customerId, status, pageable);
        return new PageResponse<>(orders.getContent().stream().map(this::toSummary).toList(),
                orders.getNumber(), orders.getSize(), orders.getTotalElements(), orders.getTotalPages());
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerOrderDetailResponse detail(UUID customerId, UUID orderId) {
        Order order = orderRepository.findByIdAndCustomerId(orderId, customerId)
                .orElseThrow(OrderManagementNotFoundException::new);
        OrderResponse snapshot = orderMapper.toResponse(order,
                orderItemRepository.findAllByOrderIdOrderByProductNameAsc(orderId));
        return toDetail(order, snapshot);
    }

    @Override
    @Transactional
    public CustomerOrderDetailResponse cancel(UUID customerId, UUID orderId) {
        // Evita que un pago u otro cambio de estado modifique la orden al mismo tiempo.
        Order order = orderRepository.findByIdAndCustomerIdForUpdate(orderId, customerId)
                .orElseThrow(OrderManagementNotFoundException::new);
        // El cliente solo puede cancelar órdenes pendientes o pagadas.
        // Si falla el reembolso, se revierten el stock y el estado de la orden.
        OrderResponse snapshot = orderManagementService.changeStatus(orderId, order.getStore().getId(),
                customerId, new ChangeOrderStatusRequest(OrderStatus.CANCELLED));
        return toDetail(order, snapshot);
    }

    private CustomerOrderSummaryResponse toSummary(Order order) {
        return new CustomerOrderSummaryResponse(order.getId(), order.getOrderNumber(),
                order.getStore().getId(), order.getStore().getName(), order.getStore().getSubdomain(),
                order.getStore().getCurrency(), order.getInvoiceType(), order.getTotal(),
                order.getStatus(), order.getCreatedAt());
    }

    private CustomerOrderDetailResponse toDetail(Order order, OrderResponse snapshot) {
        return new CustomerOrderDetailResponse(snapshot, order.getStore().getName(),
                order.getStore().getSubdomain(), order.getStore().getCurrency());
    }
}
