package com.reydiazz.bodeko.core.inventory.service;

import com.reydiazz.bodeko.core.product.core.model.entity.Product;
import com.reydiazz.bodeko.core.inventory.model.entity.Inventory;
import com.reydiazz.bodeko.core.inventory.web.request.EditLowStockThresholdRequest;
import com.reydiazz.bodeko.core.inventory.web.request.EditStockQuantityRequest;
import com.reydiazz.bodeko.core.inventory.web.response.InventoryResponse;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

public interface InventoryService {

    InventoryResponse getByProduct(UUID productId, UUID storeId);

    InventoryResponse updateQuantity(UUID productId, UUID storeId, EditStockQuantityRequest request);

    InventoryResponse updateLowStockThreshold(UUID productId, UUID storeId, EditLowStockThresholdRequest request);

    Inventory createForProduct(Product product);

    Inventory decreaseStock(UUID productId, int quantity);

    Inventory increaseStock(UUID productId, int quantity);

    Map<UUID, Integer> findQuantitiesByProductIds(Collection<UUID> productIds);

}
