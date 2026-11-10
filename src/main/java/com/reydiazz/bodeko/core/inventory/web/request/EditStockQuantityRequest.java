package com.reydiazz.bodeko.core.inventory.web.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record EditStockQuantityRequest(

        @NotNull(message = "Quantity is required")
        @Min(value = 0, message = "Quantity cannot be negative")
        Integer quantity

) {
}
