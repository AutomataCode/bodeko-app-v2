package com.reydiazz.bodeko.core.order.core.web.controller;

import com.reydiazz.bodeko.core.order.core.service.OrderService;
import com.reydiazz.bodeko.core.order.core.util.OrderRouters;
import com.reydiazz.bodeko.core.order.core.web.request.CheckoutRequest;
import com.reydiazz.bodeko.core.order.core.web.response.OrderResponse;
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
@RequestMapping(OrderRouters.API_BASE)
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final com.reydiazz.bodeko.shared.idempotency.IdempotencyService idempotency;

    @org.springframework.web.bind.annotation.GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getByCustomer(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable UUID storeId, @PathVariable UUID orderId) {
        return ResponseEntity.ok(orderService.getByCustomer(principal.user().getId(), storeId, orderId));
    }

    @PostMapping
    public ResponseEntity<OrderResponse> checkout(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable UUID storeId,
            @org.springframework.web.bind.annotation.RequestHeader(value="Idempotency-Key",required=false) String idempotencyKey,
            @Valid @RequestBody CheckoutRequest request) {
        OrderResponse response = idempotency.execute(principal.user().getId(), "checkout:"+principal.user().getId()+":"+storeId,
                "checkout",idempotencyKey,request,OrderResponse.class,false,
                () -> orderService.checkout(principal.user().getId(),storeId,request));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
