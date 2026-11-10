package com.reydiazz.bodeko.core.payment.gateway;

public record GatewayChargeResult(
        String transactionId,
        boolean approved,
        String message
) {
}
