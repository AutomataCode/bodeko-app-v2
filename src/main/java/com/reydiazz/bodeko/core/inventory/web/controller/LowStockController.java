package com.reydiazz.bodeko.core.inventory.web.controller;

import com.reydiazz.bodeko.core.inventory.repository.InventoryRepository;
import com.reydiazz.bodeko.core.inventory.web.response.LowStockResponse;
import com.reydiazz.bodeko.core.store.exception.StoreNotFoundException;
import com.reydiazz.bodeko.core.store.repository.StoreRepository;
import com.reydiazz.bodeko.security.config.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

/** Lista las variantes que necesitan reposición para la tienda del administrador. */
@RestController
@RequestMapping("/api/stores/{storeId}/inventory/low-stock")
@RequiredArgsConstructor
public class LowStockController {
    private final InventoryRepository inventory;
    private final StoreRepository stores;

    @GetMapping
    public List<LowStockResponse> list(@PathVariable UUID storeId, @AuthenticationPrincipal UserPrincipal principal) {
        if (!stores.existsByIdAndOwnerId(storeId, principal.user().getId())) throw new StoreNotFoundException();
        return inventory.findLowStockByStore(storeId).stream().map(item -> new LowStockResponse(
                item.getProduct().getId(), item.getVariantId(), item.getProduct().getName(), item.getVariant().getSku(),
                item.getVariant().getOptions(), item.getQuantity(), item.getLowStockThreshold(),
                Math.max(item.getLowStockThreshold() - item.getQuantity() + 1, 0), item.getUpdatedAt())).toList();
    }
}
