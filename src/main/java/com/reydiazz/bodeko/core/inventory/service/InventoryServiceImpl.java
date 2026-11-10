package com.reydiazz.bodeko.core.inventory.service;

import com.github.f4b6a3.uuid.UuidCreator;
import com.reydiazz.bodeko.core.product.core.model.entity.Product;
import com.reydiazz.bodeko.core.inventory.component.InventoryMapper;
import com.reydiazz.bodeko.core.inventory.exception.InventoryInsufficientStockException;
import com.reydiazz.bodeko.core.inventory.exception.InventoryNotFoundException;
import com.reydiazz.bodeko.core.inventory.model.entity.Inventory;
import com.reydiazz.bodeko.core.inventory.repository.InventoryRepository;
import com.reydiazz.bodeko.core.inventory.web.request.EditLowStockThresholdRequest;
import com.reydiazz.bodeko.core.inventory.web.request.EditStockQuantityRequest;
import com.reydiazz.bodeko.core.inventory.web.response.InventoryResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final InventoryMapper inventoryMapper;

    @Override
    @Transactional(readOnly = true)
    public InventoryResponse getByProduct(UUID productId, UUID storeId) {
        Inventory inventory = findEntityByProduct(productId, storeId);
        return inventoryMapper.toResponse(inventory);
    }

    @Override
    @Transactional
    public InventoryResponse updateQuantity(UUID productId, UUID storeId, EditStockQuantityRequest request) {
        Inventory inventory = findEntityByProduct(productId, storeId);
        inventory.updateQuantity(request.quantity());
        return inventoryMapper.toResponse(inventory);
    }

    @Override
    @Transactional
    public InventoryResponse updateLowStockThreshold(UUID productId, UUID storeId, EditLowStockThresholdRequest request) {
        Inventory inventory = findEntityByProduct(productId, storeId);
        inventory.updateLowStockThreshold(request.lowStockThreshold());
        return inventoryMapper.toResponse(inventory);
    }

    @Override
    @Transactional
    public Inventory createForProduct(Product product) {
        Inventory inventory = Inventory.builder()
                .id(UuidCreator.getTimeOrderedEpoch())
                .product(product)
                .updatedAt(OffsetDateTime.now())
                .build();
        return inventoryRepository.save(inventory);
    }

    @Override
    @Transactional
    public Inventory decreaseStock(UUID productId, int quantity) {
        Inventory inventory = inventoryRepository.findByProductIdForUpdate(productId)
                .orElseThrow(InventoryNotFoundException::new);
        if (inventory.getQuantity() < quantity) {
            throw new InventoryInsufficientStockException(inventory.getProduct().getName());
        }
        inventory.decreaseQuantity(quantity);
        if (inventory.isLowStock()) {
            log.info("Low stock alert: product {} has {} units left (threshold {})",
                    productId, inventory.getQuantity(), inventory.getLowStockThreshold());
        }
        return inventory;
    }

    @Override
    @Transactional
    public Inventory increaseStock(UUID productId, int quantity) {
        Inventory inventory = inventoryRepository.findByProductIdForUpdate(productId)
                .orElseThrow(InventoryNotFoundException::new);
        inventory.increaseQuantity(quantity);
        return inventory;
    }

    @Override
    @Transactional(readOnly = true)
    public Map<UUID, Integer> findQuantitiesByProductIds(Collection<UUID> productIds) {
        if (productIds.isEmpty()) {
            return Map.of();
        }
        return inventoryRepository.findAllByProductIdIn(productIds).stream()
                .collect(Collectors.toMap(
                        inventory -> inventory.getProduct().getId(),
                        Inventory::getQuantity
                ));
    }

    private Inventory findEntityByProduct(UUID productId, UUID storeId) {
        return inventoryRepository.findByProductIdAndStoreId(productId, storeId)
                .orElseThrow(InventoryNotFoundException::new);
    }

}
