package com.reydiazz.bodeko.core.inventory.service;

import com.reydiazz.bodeko.core.product.core.model.entity.Product;
import com.reydiazz.bodeko.core.inventory.model.entity.Inventory;
import com.reydiazz.bodeko.core.inventory.model.enums.InventoryMovementType;
import com.reydiazz.bodeko.core.inventory.web.request.EditLowStockThresholdRequest;
import com.reydiazz.bodeko.core.inventory.web.request.EditStockQuantityRequest;
import com.reydiazz.bodeko.core.inventory.web.response.InventoryResponse;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

public interface InventoryService {

    InventoryResponse getByProduct(UUID productId, UUID storeId);

    InventoryResponse updateQuantity(UUID productId, UUID storeId, EditStockQuantityRequest request,
                                     UUID actorId, String actorName, String actorEmail);

    InventoryResponse updateLowStockThreshold(UUID productId, UUID storeId, EditLowStockThresholdRequest request);

    Inventory createForProduct(Product product);

    Inventory decreaseStock(UUID productId, int quantity);

    Inventory increaseStock(UUID productId, int quantity);

    Inventory lockVariantStock(UUID variantId);

    Inventory decreaseVariantStock(UUID variantId, int quantity);

    Inventory increaseVariantStock(UUID variantId, int quantity);

    InventoryResponse updateVariantQuantity(UUID productId, UUID variantId, UUID storeId, EditStockQuantityRequest request,
                                             UUID actorId, String actorName, String actorEmail);

    InventoryResponse registerShrinkage(UUID storeId, UUID productId, UUID variantId, int quantity,
                                        InventoryMovementType type, String reason, UUID actorId,
                                        String actorName, String actorEmail);


    Map<UUID, Integer> findQuantitiesByProductIds(Collection<UUID> productIds);

}
