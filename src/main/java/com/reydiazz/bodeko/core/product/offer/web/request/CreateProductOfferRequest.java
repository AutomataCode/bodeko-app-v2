package com.reydiazz.bodeko.core.product.offer.web.request;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
/** Datos para crear una oferta porcentual con una ventana de vigencia. */
public record CreateProductOfferRequest(
    @NotNull(message="El porcentaje es obligatorio; usa discountPercentage")
    @DecimalMin(value="0.01",message="El descuento mínimo es 0.01 por ciento")
    @DecimalMax(value="99.99",message="El descuento máximo es 99.99 por ciento")
    @Digits(integer=2,fraction=2,message="El porcentaje admite dos decimales") BigDecimal discountPercentage,
    @NotNull(message="La fecha inicial es obligatoria") OffsetDateTime startsAt,
    @NotNull(message="La fecha final es obligatoria") OffsetDateTime endsAt) {}
