package com.reydiazz.bodeko.core.product.offer.web.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record CreateProductOfferRequest(

        @NotNull(message = "Offer price is required")
        @DecimalMin(value = "0.00", message = "Offer price cannot be negative")
        @Digits(integer = 10, fraction = 2, message = "Offer price must have at most 10 integer digits and 2 decimals")
        BigDecimal offerPrice,

        @NotNull(message = "Start date is required")
        OffsetDateTime startsAt,

        @NotNull(message = "End date is required")
        OffsetDateTime endsAt

) {
}
