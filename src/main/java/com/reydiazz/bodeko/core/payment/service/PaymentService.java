package com.reydiazz.bodeko.core.payment.service;

import com.reydiazz.bodeko.core.payment.web.request.CreatePaymentRequest;
import com.reydiazz.bodeko.core.payment.web.response.PaymentResponse;

import java.util.UUID;

public interface PaymentService {

    PaymentResponse pay(UUID userId, UUID storeId, UUID orderId, CreatePaymentRequest request);

    void refundApprovedPayment(UUID orderId);

}
