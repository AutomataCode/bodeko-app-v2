package com.reydiazz.bodeko.core.inventory.web.controller;

import com.reydiazz.bodeko.core.inventory.service.InventoryService;
import com.reydiazz.bodeko.core.inventory.util.InventoryRouters;
import com.reydiazz.bodeko.core.inventory.web.request.EditLowStockThresholdRequest;
import com.reydiazz.bodeko.core.inventory.web.request.EditStockQuantityRequest;
import com.reydiazz.bodeko.core.inventory.web.response.InventoryResponse;
import com.reydiazz.bodeko.security.config.UserPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(InventoryRouters.API_BASE)
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping
    public ResponseEntity<InventoryResponse> getByProduct(
            @PathVariable UUID storeId,
            @PathVariable UUID productId) {
        InventoryResponse response = inventoryService.getByProduct(productId, storeId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping(InventoryRouters.QUANTITY)
    public ResponseEntity<InventoryResponse> updateQuantity(
            @PathVariable UUID storeId,
            @PathVariable UUID productId,
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody EditStockQuantityRequest request) {
        InventoryResponse response = inventoryService.updateQuantity(productId, storeId, request,
                principal.user().getId(), principal.user().getFullName(), principal.user().getEmail());
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/variants/{variantId}/quantity")
    public ResponseEntity<InventoryResponse> updateVariantQuantity(
            @PathVariable UUID storeId,
            @PathVariable UUID productId,
            @PathVariable UUID variantId,
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody EditStockQuantityRequest request) {
        return ResponseEntity.ok(inventoryService.updateVariantQuantity(productId, variantId, storeId, request,
                principal.user().getId(), principal.user().getFullName(), principal.user().getEmail()));
    }

    @PatchMapping(InventoryRouters.LOW_STOCK_THRESHOLD)
    public ResponseEntity<InventoryResponse> updateLowStockThreshold(
            @PathVariable UUID storeId,
            @PathVariable UUID productId,
            @Valid @RequestBody EditLowStockThresholdRequest request) {
        InventoryResponse response = inventoryService.updateLowStockThreshold(productId, storeId, request);
        return ResponseEntity.ok(response);
    }

}
