package com.reydiazz.bodeko.core.catalog.web.response;

import java.math.BigDecimal;
import java.util.UUID;

public record CatalogProductResponse(
        UUID id,
        UUID categoryId,
        String name,
        String description,
        String imageUrl,
        BigDecimal basePrice,
        BigDecimal offerPrice,
        boolean inStock
) {
}
