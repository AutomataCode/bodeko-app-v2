package com.reydiazz.bodeko.core.inventory.web.response;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

/** Variante activa que llegó a su umbral de reposición. */
public record LowStockResponse(UUID productId, UUID variantId, String productName, String sku,
                               Map<String, String> options, int quantity, int threshold, int suggestedQuantity,
                               OffsetDateTime updatedAt) {}
