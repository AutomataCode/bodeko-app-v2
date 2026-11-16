package com.reydiazz.bodeko.core.product.variant.web;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Map;

public record CreateProductVariantRequest(
        @NotBlank @Size(max = 80) @jakarta.validation.constraints.Pattern(regexp = "^[A-Za-z0-9._-]+$") String sku,
        @NotEmpty @Size(max = 8) Map<@NotBlank @Size(max = 40) String, @NotBlank @Size(max = 80) String> options,
        @NotNull @DecimalMin("0.01") @jakarta.validation.constraints.DecimalMax("100000.00") @Digits(integer = 10, fraction = 2) BigDecimal basePrice
) {}
