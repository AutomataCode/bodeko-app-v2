package com.reydiazz.bodeko.core.procurement.repository;

import com.reydiazz.bodeko.core.procurement.model.PurchaseOrder;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, UUID> {
    @Query("select distinct p from PurchaseOrder p join fetch p.supplier left join fetch p.items where p.store.id = :storeId order by p.createdAt desc")
    List<PurchaseOrder> findRecentByStore(@Param("storeId") UUID storeId);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from PurchaseOrder p join fetch p.supplier where p.id = :id and p.store.id = :storeId")
    Optional<PurchaseOrder> findByIdAndStoreIdForUpdate(@Param("id") UUID id, @Param("storeId") UUID storeId);
    Optional<PurchaseOrder> findByIdAndStoreId(UUID id, UUID storeId);
}
