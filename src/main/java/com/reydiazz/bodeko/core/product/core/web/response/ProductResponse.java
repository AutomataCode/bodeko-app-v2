package com.reydiazz.bodeko.core.product.core.web.response;

import com.reydiazz.bodeko.core.product.core.model.enums.ProductStatus;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record ProductResponse(
        UUID id,
        UUID categoryId,
        String name,
        String description,
        BigDecimal basePrice,
        String imageUrl,
        boolean taxExempt,
        ProductStatus status,
        OffsetDateTime createdAt
) {
}
