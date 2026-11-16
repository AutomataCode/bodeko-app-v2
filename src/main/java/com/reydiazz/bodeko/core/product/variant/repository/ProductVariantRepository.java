package com.reydiazz.bodeko.core.product.variant.repository;

import com.reydiazz.bodeko.core.product.variant.model.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface ProductVariantRepository extends JpaRepository<ProductVariant, UUID> {
    List<ProductVariant> findAllByProductIdOrderByIsDefaultDescSkuAsc(UUID productId);
    List<ProductVariant> findAllByProductIdInAndIsActiveTrue(Collection<UUID> productIds);
    Optional<ProductVariant> findByIdAndProductIdAndStoreId(UUID id, UUID productId, UUID storeId);
    Optional<ProductVariant> findByIdAndStoreId(UUID id, UUID storeId);
    Optional<ProductVariant> findByProductIdAndIsDefaultTrue(UUID productId);
    boolean existsByStoreIdAndSkuIgnoreCase(UUID storeId, String sku);
    boolean existsByProductIdAndOptionsKey(UUID productId, String optionsKey);
    boolean existsByStoreIdAndSkuIgnoreCaseAndIdNot(UUID storeId, String sku, UUID id);
    boolean existsByProductIdAndOptionsKeyAndIdNot(UUID productId, String optionsKey, UUID id);
    boolean existsByIdAndProductIdAndStoreId(UUID id, UUID productId, UUID storeId);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select v from ProductVariant v where v.id = :id and v.storeId = :storeId")
    Optional<ProductVariant> findByIdAndStoreIdForUpdate(@Param("id") UUID id, @Param("storeId") UUID storeId);
}
