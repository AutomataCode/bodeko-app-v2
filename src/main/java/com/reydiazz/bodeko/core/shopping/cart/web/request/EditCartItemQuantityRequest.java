package com.reydiazz.bodeko.core.shopping.cart.web.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record EditCartItemQuantityRequest(

        @NotNull(message = "Quantity is required")
        @Min(value = 1, message = "Quantity must be greater than zero")
        @jakarta.validation.constraints.Max(value = 10000, message = "La cantidad máxima es 10000")
        Integer quantity

) {
}
