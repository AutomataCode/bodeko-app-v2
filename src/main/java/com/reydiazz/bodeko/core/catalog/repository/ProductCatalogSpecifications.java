package com.reydiazz.bodeko.core.catalog.repository;

import com.reydiazz.bodeko.core.inventory.model.entity.Inventory;
import com.reydiazz.bodeko.core.product.core.model.entity.Product;
import com.reydiazz.bodeko.core.product.core.model.enums.ProductStatus;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;

import java.util.Locale;
import java.util.UUID;

@UtilityClass
public class ProductCatalogSpecifications {

    private final char LIKE_ESCAPE = '\\';

    public Specification<Product> activeInStore(UUID storeId) {
        return (root, query, builder) -> builder.and(
                builder.equal(root.get("store").get("id"), storeId),
                builder.equal(root.get("status"), ProductStatus.ACTIVE)
        );
    }

    public Specification<Product> inCategory(UUID categoryId) {
        return (root, query, builder) -> builder.equal(root.get("category").get("id"), categoryId);
    }

    public Specification<Product> nameContains(String term) {
        String pattern = "%" + escapeLike(term.trim().toLowerCase(Locale.ROOT)) + "%";
        return (root, query, builder) -> builder.like(builder.lower(root.get("name")), pattern, LIKE_ESCAPE);
    }

    public Specification<Product> withStock() {
        return (root, query, builder) -> {
            Subquery<Integer> subquery = query.subquery(Integer.class);
            Root<Inventory> inventory = subquery.from(Inventory.class);
            subquery.select(builder.literal(1)).where(
                    builder.equal(inventory.get("product").get("id"), root.get("id")),
                    builder.greaterThan(inventory.<Integer>get("quantity"), 0)
            );
            return builder.exists(subquery);
        };
    }

    private String escapeLike(String value) {
        return value
                .replace("\\", "\\\\")
                .replace("%", "\\%")
                .replace("_", "\\_");
    }

}
