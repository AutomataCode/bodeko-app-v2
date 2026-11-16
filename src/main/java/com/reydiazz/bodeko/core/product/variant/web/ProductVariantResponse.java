package com.reydiazz.bodeko.core.product.variant.web;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

public record ProductVariantResponse(UUID id, UUID productId, String sku, Map<String, String> options,
                                     BigDecimal basePrice, int quantity, boolean isDefault, boolean active) {}
