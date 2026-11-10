package com.reydiazz.bodeko.core.catalog.web.response;

import com.reydiazz.bodeko.core.store.model.enums.TypeCurrency;
import java.math.BigDecimal;
import java.util.UUID;

/** Tienda destacada y cantidad de órdenes vendidas, sin datos privados. */
public record TopSellingStoreResponse(
        UUID id, String name, String subdomain, TypeCurrency currency, BigDecimal taxRate, long salesCount) {}
