package com.reydiazz.bodeko.core.payment.component;

import com.reydiazz.bodeko.core.order.core.model.entity.Order;
import com.reydiazz.bodeko.core.payment.model.entity.Payment;
import com.reydiazz.bodeko.core.payment.web.response.PaymentResponse;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapper {

    public PaymentResponse toResponse(Payment payment, Order order, String message) {
        return new PaymentResponse(
                payment.getId(),
                order.getId(),
                order.getOrderNumber(),
                payment.getProvider(),
                payment.getTransactionId(),
                payment.getAmount(),
                payment.getStatus(),
                order.getStatus(),
                message
        );
    }

}
