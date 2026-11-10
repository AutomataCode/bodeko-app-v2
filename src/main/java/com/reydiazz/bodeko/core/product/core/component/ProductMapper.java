package com.reydiazz.bodeko.core.product.core.component;

import com.reydiazz.bodeko.core.product.core.model.entity.Product;
import com.reydiazz.bodeko.core.product.core.web.response.ProductResponse;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public ProductResponse toResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getCategory() != null ? product.getCategory().getId() : null,
                product.getName(),
                product.getDescription(),
                product.getBasePrice(),
                product.getImageUrl(),
                product.isTaxExempt(),
                product.getStatus(),
                product.getCreatedAt()
        );
    }

}
