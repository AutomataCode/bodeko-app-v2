package com.reydiazz.bodeko.core.inventory.web.controller;

import com.reydiazz.bodeko.core.inventory.model.enums.InventoryMovementType;
import com.reydiazz.bodeko.core.inventory.service.InventoryMovementService;
import com.reydiazz.bodeko.core.inventory.service.InventoryService;
import com.reydiazz.bodeko.core.inventory.web.request.RegisterShrinkageRequest;
import com.reydiazz.bodeko.core.inventory.web.response.InventoryMovementResponse;
import com.reydiazz.bodeko.core.inventory.web.response.InventoryResponse;
import com.reydiazz.bodeko.security.config.UserPrincipal;
import com.reydiazz.bodeko.shared.response.PageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.UUID;

/** Registra y consulta mermas únicamente en la tienda administrada por el usuario. */
@RestController
@RequestMapping("/api/stores/{storeId}/inventory/shrinkages")
@RequiredArgsConstructor
public class InventoryShrinkageController {
    private final InventoryService inventoryService;
    private final InventoryMovementService movementService;

    /** Descuenta unidades defectuosas o perdidas y las deja en el kardex. */
    @PostMapping
    public InventoryResponse register(@PathVariable UUID storeId,
                                      @Valid @RequestBody RegisterShrinkageRequest request,
                                      @AuthenticationPrincipal UserPrincipal principal) {
        return inventoryService.registerShrinkage(storeId, request.productId(), request.variantId(),
                request.quantity(), request.type(), request.reason(), principal.user().getId(),
                principal.user().getFullName(), principal.user().getEmail());
    }

    /** Lista mermas de la tienda con filtros y paginación. */
    @GetMapping
    public PageResponse<InventoryMovementResponse> list(
            @PathVariable UUID storeId,
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) InventoryMovementType type,
            @RequestParam(required = false) String actor,
            @RequestParam(required = false) String reference,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime to,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return movementService.searchShrinkages(storeId, principal.user().getId(), search, type, actor, reference, from, to, page, size);
    }
}
