package com.reydiazz.bodeko.core.order.management.web.controller;

import com.reydiazz.bodeko.core.order.core.model.enums.OrderStatus;
import com.reydiazz.bodeko.core.order.core.web.response.OrderResponse;
import com.reydiazz.bodeko.core.order.management.service.OrderManagementService;
import com.reydiazz.bodeko.core.order.management.util.OrderManagementRouters;
import com.reydiazz.bodeko.core.order.management.web.request.ChangeOrderStatusRequest;
import com.reydiazz.bodeko.core.order.management.web.response.OrderSummaryResponse;
import com.reydiazz.bodeko.security.config.UserPrincipal;
import com.reydiazz.bodeko.shared.response.PageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(OrderManagementRouters.API_BASE)
@RequiredArgsConstructor
public class OrderManagementController {

    private final OrderManagementService orderManagementService;
    private final com.reydiazz.bodeko.shared.idempotency.IdempotencyService idempotency;

    @GetMapping
    public ResponseEntity<PageResponse<OrderSummaryResponse>> getAll(
            @PathVariable UUID storeId,
            @RequestParam(required = false) OrderStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        PageResponse<OrderSummaryResponse> response = orderManagementService.getAll(storeId, status, page, size);
        return ResponseEntity.ok(response);
    }

    @GetMapping(OrderManagementRouters.ORDER_ID)
    public ResponseEntity<OrderResponse> getById(
            @PathVariable UUID storeId,
            @PathVariable UUID orderId) {
        OrderResponse response = orderManagementService.getById(orderId, storeId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping(OrderManagementRouters.STATUS)
    public ResponseEntity<OrderResponse> changeStatus(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable UUID storeId,
            @PathVariable UUID orderId,
            @RequestHeader(value="Idempotency-Key",required=false) String idempotencyKey,
            @Valid @RequestBody ChangeOrderStatusRequest request) {
        orderManagementService.getById(orderId,storeId);
        OrderResponse response = idempotency.execute(principal.user().getId(),"order:"+orderId,"admin-status",idempotencyKey,request,OrderResponse.class,true,
            () -> orderManagementService.changeStatus(orderId,storeId,principal.user().getId(),request));
        return ResponseEntity.ok(response);
    }

}
