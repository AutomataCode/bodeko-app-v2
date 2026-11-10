package com.reydiazz.bodeko.core.catalog.web.response;

import com.reydiazz.bodeko.core.store.model.enums.TypeCurrency;
import java.math.BigDecimal;
import java.util.UUID;

/** Datos públicos de la tienda, sin información del propietario ni datos fiscales. */
public record PublicStoreResponse(UUID id, String name, String subdomain, TypeCurrency currency, BigDecimal taxRate) {}
