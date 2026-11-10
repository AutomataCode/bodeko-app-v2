package com.reydiazz.bodeko.core.notification.service;

import com.reydiazz.bodeko.core.notification.component.OrderEmailBuilder;
import com.reydiazz.bodeko.core.notification.gateway.EmailSender;
import com.reydiazz.bodeko.core.order.core.model.entity.Order;
import com.reydiazz.bodeko.core.order.core.repository.OrderRepository;
import com.reydiazz.bodeko.core.order.items.model.entity.OrderItem;
import com.reydiazz.bodeko.core.order.items.repository.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderEmailBuilder orderEmailBuilder;
    private final EmailSender emailSender;

    @Override
    @Transactional(readOnly = true)
    public void sendOrderPaidConfirmation(UUID orderId) {
        try {
            Order order = orderRepository.findById(orderId).orElse(null);
            if (order == null) {
                log.warn("Order {} not found, confirmation email skipped", orderId);
                return;
            }
            List<OrderItem> items = orderItemRepository.findAllByOrderIdOrderByProductNameAsc(orderId);
            emailSender.send(orderEmailBuilder.buildOrderPaid(order, items));
        } catch (RuntimeException exception) {
            log.error("Could not send the confirmation email of order {}", orderId, exception);
        }
    }

}
