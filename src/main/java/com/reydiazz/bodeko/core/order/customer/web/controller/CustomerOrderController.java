package com.reydiazz.bodeko.core.order.customer.web.controller;

import com.reydiazz.bodeko.core.order.core.model.enums.OrderStatus;
import com.reydiazz.bodeko.core.order.customer.service.CustomerOrderService;
import com.reydiazz.bodeko.core.order.customer.web.response.CustomerOrderDetailResponse;
import com.reydiazz.bodeko.core.order.customer.web.response.CustomerOrderSummaryResponse;
import com.reydiazz.bodeko.security.authorization.util.CustomerRoutes;
import com.reydiazz.bodeko.security.config.UserPrincipal;
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

    @GetMapping
    public PageResponse<CustomerOrderSummaryResponse> list(@AuthenticationPrincipal UserPrincipal principal,
            @RequestParam(required = false) OrderStatus status,
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return customerOrderService.list(principal.user().getId(), status, page, size);
    }

    @GetMapping("/{orderId}")
    public CustomerOrderDetailResponse detail(@AuthenticationPrincipal UserPrincipal principal, @PathVariable UUID orderId) {
        return customerOrderService.detail(principal.user().getId(), orderId);
    }

    @PostMapping("/{orderId}/cancel")
    public CustomerOrderDetailResponse cancel(@AuthenticationPrincipal UserPrincipal principal, @PathVariable UUID orderId) {
        return customerOrderService.cancel(principal.user().getId(), orderId);
    }
}
