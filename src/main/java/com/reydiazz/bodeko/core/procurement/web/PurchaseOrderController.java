package com.reydiazz.bodeko.core.procurement.web;

import com.reydiazz.bodeko.core.procurement.service.PurchaseOrderService;
import com.reydiazz.bodeko.core.procurement.web.PurchaseOrderRequests.*;
import com.reydiazz.bodeko.core.procurement.web.PurchaseOrderResponses.*;
import com.reydiazz.bodeko.security.config.UserPrincipal;
import com.reydiazz.bodeko.shared.idempotency.IdempotencyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

/** Endpoints de abastecimiento interno de la tienda. */
@RestController
@RequestMapping("/api/stores/{storeId}/procurement")
@RequiredArgsConstructor
public class PurchaseOrderController {
    private final PurchaseOrderService service;
    private final IdempotencyService idempotency;

    @GetMapping("/suppliers")
    public List<SupplierResponse> suppliers(@PathVariable UUID storeId, @AuthenticationPrincipal UserPrincipal principal) {
        return service.listSuppliers(storeId, principal.user().getId());
    }

    @PostMapping("/suppliers")
    public SupplierResponse createSupplier(@PathVariable UUID storeId, @AuthenticationPrincipal UserPrincipal principal,
                                          @Valid @RequestBody SupplierRequest request) {
        return service.createSupplier(storeId, principal.user().getId(), request);
    }

    @GetMapping("/orders")
    public List<PurchaseOrderResponse> list(@PathVariable UUID storeId, @AuthenticationPrincipal UserPrincipal principal) {
        return service.list(storeId, principal.user().getId());
    }

    @PostMapping("/orders")
    public PurchaseOrderResponse create(@PathVariable UUID storeId, @AuthenticationPrincipal UserPrincipal principal,
                                        @RequestHeader(value = "Idempotency-Key", required = false) String key,
                                        @Valid @RequestBody CreatePurchaseOrderRequest request) {
        return idempotency.execute(principal.user().getId(), "procurement:" + storeId, "create", key, request,
                PurchaseOrderResponse.class, false, () -> service.create(storeId, principal.user().getId(), request));
    }

    @PostMapping("/orders/{orderId}/receive")
    public PurchaseOrderResponse receive(@PathVariable UUID storeId, @PathVariable UUID orderId,
                                         @AuthenticationPrincipal UserPrincipal principal,
                                         @RequestHeader(value = "Idempotency-Key", required = false) String key,
                                         @Valid @RequestBody ReceivePurchaseOrderRequest request) {
        return idempotency.execute(principal.user().getId(), "purchase-order:" + orderId, "receive", key, request,
                PurchaseOrderResponse.class, false, () -> service.receive(storeId, principal.user().getId(), principal.user().getFullName(), principal.user().getEmail(), orderId, request));
    }
}
