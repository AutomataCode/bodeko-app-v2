package com.reydiazz.bodeko.core.payment.web.controller;

import com.reydiazz.bodeko.core.payment.exception.PaymentDeclinedException;
import com.reydiazz.bodeko.core.payment.model.enums.PaymentStatus;
import com.reydiazz.bodeko.core.payment.service.PaymentService;
import com.reydiazz.bodeko.core.payment.util.PaymentRouters;
import com.reydiazz.bodeko.core.payment.web.request.CreatePaymentRequest;
import com.reydiazz.bodeko.core.payment.web.response.PaymentResponse;
import com.reydiazz.bodeko.security.config.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping(PaymentRouters.API_BASE)
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final com.reydiazz.bodeko.core.order.core.service.OrderService orderService;
    private final com.reydiazz.bodeko.shared.idempotency.IdempotencyService idempotency;

    @PostMapping
    public ResponseEntity<PaymentResponse> pay(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable UUID storeId,
            @PathVariable UUID orderId,
            @org.springframework.web.bind.annotation.RequestHeader(value="Idempotency-Key",required=false) String idempotencyKey,
            @Valid @RequestBody CreatePaymentRequest request) {
        orderService.getByCustomer(principal.user().getId(),storeId,orderId);
        PaymentResponse response = idempotency.execute(principal.user().getId(),"order:"+orderId,"pay",idempotencyKey,request,PaymentResponse.class,true,
                () -> paymentService.pay(principal.user().getId(),storeId,orderId,request));
        if (response.status() == PaymentStatus.FAILED) {
            throw new PaymentDeclinedException(response.message());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
