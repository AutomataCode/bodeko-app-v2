package com.reydiazz.bodeko.core.product.core.repository;

import com.reydiazz.bodeko.core.product.core.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID>, JpaSpecificationExecutor<Product> {
    @org.springframework.data.jpa.repository.Lock(jakarta.persistence.LockModeType.PESSIMISTIC_WRITE)
    @org.springframework.data.jpa.repository.Query("select p from Product p where p.id = :id and p.store.id = :storeId")
    java.util.Optional<Product> findByIdAndStoreIdForUpdate(@org.springframework.data.repository.query.Param("id") UUID id, @org.springframework.data.repository.query.Param("storeId") UUID storeId);

    @org.springframework.data.jpa.repository.Query(value = "select exists(select 1 from products where store_id=:storeId and lower(regexp_replace(btrim(name), '\\s+', ' ', 'g'))=:name and (cast(:excluded as uuid) is null or id<>cast(:excluded as uuid)))", nativeQuery = true)
    boolean existsNormalizedName(@org.springframework.data.repository.query.Param("storeId") UUID storeId, @org.springframework.data.repository.query.Param("name") String name, @org.springframework.data.repository.query.Param("excluded") UUID excluded);


    boolean existsByCategoryIdAndStoreId(UUID categoryId, UUID storeId);

    List<Product> findAllByStoreIdOrderByCreatedAtDesc(UUID storeId);

    Optional<Product> findByIdAndStoreId(UUID id, UUID storeId);

}
