package com.reydiazz.bodeko.core.store.web.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record EditTaxRateRequest(

        @NotNull(message = "Tax rate is required")
        @DecimalMin(value = "0.00", message = "Tax rate cannot be negative")
        @DecimalMax(value = "1.00", message = "Tax rate cannot be greater than 1.00 (100%)")
        @Digits(integer = 1, fraction = 2, message = "Tax rate must have at most 2 decimals")
        BigDecimal taxRate

) {
}
