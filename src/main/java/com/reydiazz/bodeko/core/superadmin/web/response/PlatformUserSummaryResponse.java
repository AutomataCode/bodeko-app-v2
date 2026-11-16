package com.reydiazz.bodeko.core.superadmin.web.response;

/** Totales de vendedores y clientes para las tarjetas del panel. */
public record PlatformUserSummaryResponse(long vendors, long activeVendors, long customers, long activeCustomers) {}
