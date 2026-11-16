package com.reydiazz.bodeko.core.superadmin.web.response;

import java.util.UUID;

/** Opción para filtrar cuentas por tienda. */
public record PlatformStoreOptionResponse(UUID id, String name, String subdomain) {}
