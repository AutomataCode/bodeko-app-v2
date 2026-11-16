package com.reydiazz.bodeko.core.inventory.web.controller;

import com.reydiazz.bodeko.core.inventory.model.enums.InventoryMovementType;
import com.reydiazz.bodeko.core.inventory.service.InventoryMovementService;
import com.reydiazz.bodeko.core.inventory.web.response.InventoryMovementResponse;
import com.reydiazz.bodeko.shared.response.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.util.UUID;

/** Endpoints de consulta del kardex de la tienda autorizada. */
@RestController
@RequestMapping("/api/stores/{storeId}/inventory/movements")
@RequiredArgsConstructor
public class InventoryMovementController {
    private final InventoryMovementService movementService;

    @GetMapping
    public PageResponse<InventoryMovementResponse> search(
            @org.springframework.web.bind.annotation.PathVariable UUID storeId,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) InventoryMovementType type,
            @RequestParam(required = false) String actor,
            @RequestParam(required = false) String reference,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime to,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return movementService.search(storeId, search, type, actor, reference, from, to, page, size);
    }
}
