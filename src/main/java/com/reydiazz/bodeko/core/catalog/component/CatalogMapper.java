package com.reydiazz.bodeko.core.catalog.component;

import com.reydiazz.bodeko.core.catalog.web.response.CatalogProductResponse;
import com.reydiazz.bodeko.core.product.core.model.entity.Product;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class CatalogMapper {

    public CatalogProductResponse toResponse(Product product, BigDecimal offerPrice, boolean inStock) {
        return new CatalogProductResponse(
                product.getId(),
                product.getCategory() != null ? product.getCategory().getId() : null,
                product.getName(),
                product.getDescription(),
                product.getImageUrl(),
                product.getBasePrice(),
                offerPrice,
                inStock
        );
    }

}
