package com.reydiazz.bodeko.core.inventory.repository;

import com.reydiazz.bodeko.core.inventory.model.entity.Inventory;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InventoryRepository extends JpaRepository<Inventory, UUID> {

    @Query("select i from Inventory i where i.product.id = :productId and i.product.store.id = :storeId and i.variant.isDefault = true")
    Optional<Inventory> findByProductIdAndStoreId(@Param("productId") UUID productId, @Param("storeId") UUID storeId);

    Optional<Inventory> findByVariantId(UUID variantId);

    @Query("select i from Inventory i where i.variantId = :variantId and i.product.id = :productId and i.product.store.id = :storeId")
    Optional<Inventory> findByVariantAndProductAndStore(@Param("variantId") UUID variantId, @Param("productId") UUID productId, @Param("storeId") UUID storeId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select i from Inventory i where i.product.id = :productId and i.variant.isDefault = true")
    Optional<Inventory> findByProductIdForUpdate(@Param("productId") UUID productId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select i from Inventory i where i.variantId = :variantId")
    Optional<Inventory> findByVariantIdForUpdate(@Param("variantId") UUID variantId);

    List<Inventory> findAllByProductIdIn(Collection<UUID> productIds);

    List<Inventory> findAllByProductIdInAndVariantIsActiveTrue(Collection<UUID> productIds);

    List<Inventory> findAllByVariantIdIn(Collection<UUID> variantIds);

    @Query("select i from Inventory i join fetch i.product p join fetch i.variant v where p.store.id = :storeId and p.status = com.reydiazz.bodeko.core.product.core.model.enums.ProductStatus.ACTIVE and v.isActive = true and i.quantity <= i.lowStockThreshold order by i.quantity asc, p.name asc")
    List<Inventory> findLowStockByStore(@Param("storeId") UUID storeId);

}
