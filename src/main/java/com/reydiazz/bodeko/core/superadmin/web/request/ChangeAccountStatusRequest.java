package com.reydiazz.bodeko.core.superadmin.web.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/** Estado y motivo requerido para conservar trazabilidad del cambio. */
public record ChangeAccountStatusRequest(@NotNull Boolean active,
                                         @NotBlank @Size(max = 500) String reason) {}
