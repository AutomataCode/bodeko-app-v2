package com.reydiazz.bodeko.core.payment.gateway;

public interface PaymentGateway {

    String provider();

    GatewayChargeResult charge(GatewayChargeRequest request);

    GatewayRefundResult refund(GatewayRefundRequest request);

}
