package com.reydiazz.bodeko.core.payment.gateway;

public record GatewayRefundResult(
        String refundId,
        boolean accepted,
        String message
) {
}
