package com.reydiazz.bodeko.core.store.web.response;

import com.reydiazz.bodeko.core.store.model.enums.TypeCurrency;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record StoreResponse(
        UUID id,
        String name,
        String taxId,
        String legalName,
        String subdomain,
        TypeCurrency currency,
        BigDecimal taxRate,
        OffsetDateTime createdAt
) {
}
