package com.reydiazz.bodeko.core.payment.gateway;

import java.math.BigDecimal;

public record GatewayRefundRequest(
        String transactionId,
        BigDecimal amount,
        String orderNumber
) {
}
