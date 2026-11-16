package com.reydiazz.bodeko.core.inventory.web.request;

import com.reydiazz.bodeko.core.inventory.model.enums.InventoryMovementType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record EditStockQuantityRequest(

        @NotNull(message = "Quantity is required")
        @Min(value = 0, message = "Quantity cannot be negative")
        @jakarta.validation.constraints.Max(value=1000000,message="El límite de inventario es 1000000")
        Integer quantity,
        InventoryMovementType movementType,
        @jakarta.validation.constraints.Size(max = 500, message = "El motivo no puede superar 500 caracteres")
        String reason

) {
}
