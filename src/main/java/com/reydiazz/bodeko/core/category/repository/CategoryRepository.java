package com.reydiazz.bodeko.core.category.repository;

import com.reydiazz.bodeko.core.category.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {

    boolean existsByStoreIdAndNameIgnoreCase(UUID storeId, String name);

    boolean existsByStoreIdAndNameIgnoreCaseAndIdNot(UUID storeId, String name, UUID categoryId);

    List<Category> findAllByStoreId(UUID storeId);

    Optional<Category> findByIdAndStoreId(UUID id, UUID storeId);

}
