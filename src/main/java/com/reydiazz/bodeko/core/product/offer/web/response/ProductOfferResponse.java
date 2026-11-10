package com.reydiazz.bodeko.core.product.offer.web.response;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record ProductOfferResponse(
        UUID productId,
        BigDecimal offerPrice,
        OffsetDateTime startsAt,
        OffsetDateTime endsAt,
        boolean active,
        OffsetDateTime createdAt
) {
}
