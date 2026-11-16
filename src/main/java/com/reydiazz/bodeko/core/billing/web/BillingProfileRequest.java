package com.reydiazz.bodeko.core.billing.web;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/** Datos del emisor que se muestran en los comprobantes de la tienda. */
public record BillingProfileRequest(
        @NotBlank @Pattern(regexp = "^\\d{11}$", message = "El RUC debe tener 11 dígitos") String taxId,
        @NotBlank @Size(max = 200) String legalName,
        @NotBlank @Size(max = 300) String fiscalAddress
) {}
