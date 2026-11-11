package com.reydiazz.bodeko.core.inventory.web.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record EditLowStockThresholdRequest(

        @NotNull(message = "Low stock threshold is required")
        @Min(value = 0, message = "Low stock threshold cannot be negative")
        @jakarta.validation.constraints.Max(value=1000000,message="El límite de inventario es 1000000")
        Integer lowStockThreshold

) {
}
