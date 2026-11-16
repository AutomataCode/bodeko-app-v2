package com.reydiazz.bodeko.core.order.customer.web.controller;

import com.reydiazz.bodeko.core.order.core.model.enums.OrderStatus;
import com.reydiazz.bodeko.core.order.customer.service.CustomerOrderService;
import com.reydiazz.bodeko.core.order.customer.web.response.CustomerOrderDetailResponse;
import com.reydiazz.bodeko.core.order.customer.web.response.CustomerOrderSummaryResponse;
import com.reydiazz.bodeko.security.authorization.util.CustomerRoutes;
import com.reydiazz.bodeko.security.config.CustomerPrincipal;
import com.reydiazz.bodeko.shared.response.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import java.util.UUID;

/** Acciones del cliente autenticado. Su identidad se obtiene del JWT. */
@RestController
@RequestMapping(CustomerRoutes.API_BASE + "/orders")
@RequiredArgsConstructor
public class CustomerOrderController {
    private final CustomerOrderService customerOrderService;
    private final com.reydiazz.bodeko.shared.idempotency.IdempotencyService idempotency;

    @GetMapping
    public PageResponse<CustomerOrderSummaryResponse> list(@AuthenticationPrincipal CustomerPrincipal principal,
            @RequestParam(required = false) OrderStatus status,
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return customerOrderService.list(principal.customer().getId(), status, page, size);
    }

    @GetMapping("/{orderId}")
    public CustomerOrderDetailResponse detail(@AuthenticationPrincipal CustomerPrincipal principal, @PathVariable UUID orderId) {
        return customerOrderService.detail(principal.customer().getId(), orderId);
    }

    @PostMapping("/{orderId}/cancel")
    public CustomerOrderDetailResponse cancel(@AuthenticationPrincipal CustomerPrincipal principal, @PathVariable UUID orderId, @RequestHeader(value="Idempotency-Key",required=false) String idempotencyKey) {
        customerOrderService.detail(principal.customer().getId(),orderId);
        return idempotency.execute(principal.customer().getId(),"order:"+orderId,"cancel",idempotencyKey,"cancel",CustomerOrderDetailResponse.class,true,
                () -> customerOrderService.cancel(principal.customer().getId(),orderId));
    }
}
