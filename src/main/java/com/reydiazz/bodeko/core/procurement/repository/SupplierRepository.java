package com.reydiazz.bodeko.core.procurement.repository;

import com.reydiazz.bodeko.core.procurement.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SupplierRepository extends JpaRepository<Supplier, UUID> {
    List<Supplier> findAllByStoreIdAndActiveTrueOrderByName(UUID storeId);
    Optional<Supplier> findByIdAndStoreIdAndActiveTrue(UUID id, UUID storeId);
    boolean existsByStoreIdAndNameIgnoreCase(UUID storeId, String name);
}
