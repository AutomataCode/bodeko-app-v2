package com.reydiazz.bodeko.core.inventory.web.response;

import com.reydiazz.bodeko.core.inventory.model.enums.InventoryMovementType;

import java.time.OffsetDateTime;
import java.util.UUID;

/** Datos históricos que necesita la tabla del kardex. */
public record InventoryMovementResponse(
        UUID id,
        UUID productId,
        UUID variantId,
        String productName,
        String sku,
        String variantOptions,
        InventoryMovementType movementType,
        int units,
        int quantityChange,
        int quantityBefore,
        int quantityAfter,
        String referenceType,
        UUID referenceId,
        String referenceNumber,
        String reason,
        UUID actorId,
        String actorName,
        String actorEmail,
        String actorRole,
        OffsetDateTime createdAt
) {
}
