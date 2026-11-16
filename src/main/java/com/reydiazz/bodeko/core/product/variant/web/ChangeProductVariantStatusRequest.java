package com.reydiazz.bodeko.core.product.variant.web;

import jakarta.validation.constraints.NotNull;

/** Estado solicitado para habilitar o pausar la venta de una variante. */
public record ChangeProductVariantStatusRequest(@NotNull Boolean active) {
}
