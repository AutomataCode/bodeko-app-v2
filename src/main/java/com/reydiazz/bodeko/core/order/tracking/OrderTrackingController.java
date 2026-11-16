package com.reydiazz.bodeko.core.order.tracking;

import com.reydiazz.bodeko.core.order.core.web.response.OrderResponse;
import com.reydiazz.bodeko.security.config.UserPrincipal;
import com.reydiazz.bodeko.shared.idempotency.IdempotencyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

/** Endpoints de seguimiento para el administrador de la tienda. */
@RestController
@RequestMapping("/api/stores/{storeId}/orders/{orderId}/tracking")
@RequiredArgsConstructor
public class OrderTrackingController {
    private final OrderTrackingService service;
    private final IdempotencyService idempotency;

    @PatchMapping
    public OrderResponse update(@PathVariable UUID storeId, @PathVariable UUID orderId,
                                @AuthenticationPrincipal UserPrincipal principal,
                                @RequestHeader(value = "Idempotency-Key", required = false) String key,
                                @Valid @RequestBody UpdateTrackingRequest request) {
        return idempotency.execute(principal.user().getId(), "order-tracking:" + orderId, "update", key, request,
                OrderResponse.class, false, () -> service.update(storeId, principal.user().getId(), orderId, request));
    }
}
