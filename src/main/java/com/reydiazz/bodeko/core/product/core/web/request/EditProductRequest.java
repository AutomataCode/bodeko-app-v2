package com.reydiazz.bodeko.core.product.core.web.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.UUID;

public record EditProductRequest(

        @NotBlank(message = "Name is required")
        @Size(max = 200, message = "Name must have at most 200 characters")
        String name,

        String description,

        @NotNull(message = "Base price is required")
        @DecimalMin(value = "0.00", message = "Base price cannot be negative")
        @Digits(integer = 10, fraction = 2, message = "Base price must have at most 10 integer digits and 2 decimals")
        BigDecimal basePrice,

        @Size(max = 500, message = "Image URL must have at most 500 characters")
        String imageUrl,

        UUID categoryId,

        boolean taxExempt

) {
}
