package com.reydiazz.bodeko.core.inventory.service;

import com.github.f4b6a3.uuid.UuidCreator;
import com.reydiazz.bodeko.core.product.core.model.entity.Product;
import com.reydiazz.bodeko.core.inventory.component.InventoryMapper;
import com.reydiazz.bodeko.core.inventory.exception.InventoryInsufficientStockException;
import com.reydiazz.bodeko.core.inventory.exception.InventoryNotFoundException;
import com.reydiazz.bodeko.core.inventory.model.entity.Inventory;
import com.reydiazz.bodeko.core.inventory.model.enums.InventoryMovementType;
import com.reydiazz.bodeko.core.inventory.repository.InventoryRepository;
import com.reydiazz.bodeko.core.inventory.web.request.EditLowStockThresholdRequest;
import com.reydiazz.bodeko.core.inventory.web.request.EditStockQuantityRequest;
import com.reydiazz.bodeko.core.inventory.web.response.InventoryResponse;
import com.reydiazz.bodeko.core.store.repository.StoreRepository;
import com.reydiazz.bodeko.core.store.exception.StoreNotFoundException;
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
    private final InventoryMovementService movementService;
    private final StoreRepository storeRepository;

    @Override
    @Transactional(readOnly = true)
    public InventoryResponse getByProduct(UUID productId, UUID storeId) {
        Inventory inventory = findEntityByProduct(productId, storeId);
        return inventoryMapper.toResponse(inventory);
    }

    @Override
    @Transactional
    public InventoryResponse updateQuantity(UUID productId, UUID storeId, EditStockQuantityRequest request,
                                            UUID actorId, String actorName, String actorEmail) {
        Inventory inventory = inventoryRepository.findByProductIdForUpdate(productId)
            .filter(item -> item.getProduct().getStore().getId().equals(storeId)).orElseThrow(InventoryNotFoundException::new);
        int before = inventory.getQuantity();
        inventory.updateQuantity(request.quantity());
        movementService.recordManual(inventory, validateMovementType(request.movementType(), before, request.quantity()),
                before, request.quantity(), request.reason(), actorId, actorName, actorEmail);
        return inventoryMapper.toResponse(inventory);
    }

    @Override
    @Transactional
    public InventoryResponse updateLowStockThreshold(UUID productId, UUID storeId, EditLowStockThresholdRequest request) {
        Inventory inventory = inventoryRepository.findByProductIdForUpdate(productId)
            .filter(item -> item.getProduct().getStore().getId().equals(storeId)).orElseThrow(InventoryNotFoundException::new);
        inventory.updateLowStockThreshold(request.lowStockThreshold());
        return inventoryMapper.toResponse(inventory);
    }

    @Override
    @Transactional
    public Inventory createForProduct(Product product) {
        Inventory inventory = Inventory.builder()
                .id(UuidCreator.getTimeOrderedEpoch())
                .product(product)
                .variantId(product.getId())
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
    @Transactional
    public Inventory lockVariantStock(UUID variantId) {
        return inventoryRepository.findByVariantIdForUpdate(variantId).orElseThrow(InventoryNotFoundException::new);
    }

    @Override
    @Transactional
    public Inventory decreaseVariantStock(UUID variantId, int quantity) {
        Inventory inventory = lockVariantStock(variantId);
        if (inventory.getQuantity() < quantity) throw new InventoryInsufficientStockException(inventory.getProduct().getName());
        inventory.decreaseQuantity(quantity);
        return inventory;
    }

    @Override
    @Transactional
    public Inventory increaseVariantStock(UUID variantId, int quantity) {
        Inventory inventory = lockVariantStock(variantId);
        inventory.increaseQuantity(quantity);
        return inventory;
    }

    @Override
    @Transactional
    public InventoryResponse updateVariantQuantity(UUID productId, UUID variantId, UUID storeId, EditStockQuantityRequest request,
                                                   UUID actorId, String actorName, String actorEmail) {
        Inventory inventory = inventoryRepository.findByVariantIdForUpdate(variantId)
                .filter(item -> item.getProduct().getId().equals(productId) && item.getProduct().getStore().getId().equals(storeId))
                .orElseThrow(InventoryNotFoundException::new);
        int before = inventory.getQuantity();
        inventory.updateQuantity(request.quantity());
        movementService.recordManual(inventory, validateMovementType(request.movementType(), before, request.quantity()),
                before, request.quantity(), request.reason(), actorId, actorName, actorEmail);
        return inventoryMapper.toResponse(inventory);
    }

    @Override
    @Transactional
    public InventoryResponse registerShrinkage(UUID storeId, UUID productId, UUID variantId, int quantity,
                                               InventoryMovementType type, String reason, UUID actorId,
                                               String actorName, String actorEmail) {
        if (type != InventoryMovementType.DAMAGE && type != InventoryMovementType.LOSS) {
            throw new com.reydiazz.bodeko.shared.exception.BusinessException(
                    com.reydiazz.bodeko.shared.validation.ValidationError.INVALID_SHRINKAGE_TYPE);
        }
        if (!storeRepository.existsByIdAndOwnerId(storeId, actorId)) throw new StoreNotFoundException();
        Inventory inventory = inventoryRepository.findByVariantIdForUpdate(variantId)
                .filter(item -> item.getProduct().getId().equals(productId)
                        && item.getProduct().getStore().getId().equals(storeId))
                .orElseThrow(InventoryNotFoundException::new);
        int before = inventory.getQuantity();
        if (before < quantity) {
            throw new com.reydiazz.bodeko.shared.exception.BusinessException(
                    com.reydiazz.bodeko.shared.validation.ValidationError.SHRINKAGE_INSUFFICIENT_STOCK);
        }
        inventory.decreaseQuantity(quantity);
        movementService.recordManual(inventory, type, before, inventory.getQuantity(), reason.trim(),
                actorId, actorName, actorEmail);
        return inventoryMapper.toResponse(inventory);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<UUID, Integer> findQuantitiesByProductIds(Collection<UUID> productIds) {
        if (productIds.isEmpty()) {
            return Map.of();
        }
        return inventoryRepository.findAllByProductIdInAndVariantIsActiveTrue(productIds).stream()
                .collect(Collectors.toMap(
                        inventory -> inventory.getProduct().getId(),
                        Inventory::getQuantity,
                        Integer::sum
                ));
    }

    private InventoryMovementType validateMovementType(InventoryMovementType requested, int before, int after) {
        InventoryMovementType type = requested == null ? InventoryMovementType.ADJUSTMENT : requested;
        int change = after - before;
        if ((type == InventoryMovementType.STOCK_ENTRY || type == InventoryMovementType.RETURN) && change < 0)
            throw new com.reydiazz.bodeko.shared.exception.BusinessException(
                    com.reydiazz.bodeko.shared.validation.ValidationError.INVENTORY_MOVEMENT_DIRECTION);
        if ((type == InventoryMovementType.DAMAGE || type == InventoryMovementType.LOSS) && change > 0)
            throw new com.reydiazz.bodeko.shared.exception.BusinessException(
                    com.reydiazz.bodeko.shared.validation.ValidationError.INVENTORY_MOVEMENT_DIRECTION);
        return type;
    }

    private Inventory findEntityByProduct(UUID productId, UUID storeId) {
        return inventoryRepository.findByProductIdAndStoreId(productId, storeId)
                .orElseThrow(InventoryNotFoundException::new);
    }

}
