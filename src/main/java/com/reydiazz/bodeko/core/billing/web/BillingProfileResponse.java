package com.reydiazz.bodeko.core.billing.web;

import java.util.UUID;

/** Perfil fiscal visible para el administrador de las tiendas asociadas. */
public record BillingProfileResponse(UUID issuerId, String taxId, String legalName,
                                     String fiscalAddress, long linkedStores) {}
