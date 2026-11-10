package com.reydiazz.bodeko.core.inventory.web.response;

import java.time.OffsetDateTime;
import java.util.UUID;

public record InventoryResponse(
        UUID productId,
        int quantity,
        int lowStockThreshold,
        boolean lowStock,
        OffsetDateTime updatedAt
) {
}
