package com.reydiazz.bodeko.core.shopping.cart.web.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AddCartItemRequest(

        @NotNull(message = "Product is required")
        UUID productId,

        @Min(value = 1, message = "Quantity must be greater than zero")
        @jakarta.validation.constraints.Max(value = 10000, message = "La cantidad máxima es 10000")
        Integer quantity

) {
}
