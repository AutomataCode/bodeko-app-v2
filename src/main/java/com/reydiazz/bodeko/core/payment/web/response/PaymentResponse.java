package com.reydiazz.bodeko.core.payment.web.response;

import com.reydiazz.bodeko.core.order.core.model.enums.OrderStatus;
import com.reydiazz.bodeko.core.payment.model.enums.PaymentStatus;

import java.math.BigDecimal;
import java.util.UUID;

public record PaymentResponse(
        UUID id,
        UUID orderId,
        String orderNumber,
        String provider,
        String transactionId,
        BigDecimal amount,
        PaymentStatus status,
        OrderStatus orderStatus,
        String message
) {
}
