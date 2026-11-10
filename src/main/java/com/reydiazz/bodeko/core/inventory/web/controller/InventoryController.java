package com.reydiazz.bodeko.core.inventory.web.controller;

import com.reydiazz.bodeko.core.inventory.service.InventoryService;
import com.reydiazz.bodeko.core.inventory.util.InventoryRouters;
import com.reydiazz.bodeko.core.inventory.web.request.EditLowStockThresholdRequest;
import com.reydiazz.bodeko.core.inventory.web.request.EditStockQuantityRequest;
import com.reydiazz.bodeko.core.inventory.web.response.InventoryResponse;
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
            @Valid @RequestBody EditStockQuantityRequest request) {
        InventoryResponse response = inventoryService.updateQuantity(productId, storeId, request);
        return ResponseEntity.ok(response);
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
