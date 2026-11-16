package com.reydiazz.bodeko.core.catalog.web.response;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

/** Variante disponible para compra en el catálogo. */
public record CatalogVariantResponse(UUID id, String sku, Map<String, String> options,
                                      BigDecimal basePrice, BigDecimal offerPrice, int quantity) {}
