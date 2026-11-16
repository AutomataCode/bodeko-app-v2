package com.reydiazz.bodeko.core.inventory.web.request;

import com.reydiazz.bodeko.core.inventory.model.enums.InventoryMovementType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

/** Datos necesarios para registrar una pérdida física de existencias. */
public record RegisterShrinkageRequest(
        @NotNull UUID productId,
        @NotNull UUID variantId,
        @Min(1) @Max(10000) int quantity,
        @NotNull InventoryMovementType type,
        @NotBlank @Size(max = 500) String reason
) {
}
