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

    @Query("select i from Inventory i where i.product.id = :productId and i.product.store.id = :storeId")
    Optional<Inventory> findByProductIdAndStoreId(@Param("productId") UUID productId, @Param("storeId") UUID storeId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select i from Inventory i where i.product.id = :productId")
    Optional<Inventory> findByProductIdForUpdate(@Param("productId") UUID productId);

    List<Inventory> findAllByProductIdIn(Collection<UUID> productIds);

}
