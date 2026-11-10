package com.reydiazz.bodeko.core.payment.gateway;

import java.math.BigDecimal;

public record GatewayChargeRequest(
        BigDecimal amount,
        String currency,
        String email,
        String sourceId,
        String orderNumber
) {
}
