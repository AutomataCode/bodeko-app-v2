package com.reydiazz.bodeko.core.product.core.repository;

import com.reydiazz.bodeko.core.product.core.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID>, JpaSpecificationExecutor<Product> {

    boolean existsByCategoryIdAndStoreId(UUID categoryId, UUID storeId);

    List<Product> findAllByStoreIdOrderByCreatedAtDesc(UUID storeId);

    Optional<Product> findByIdAndStoreId(UUID id, UUID storeId);

}
