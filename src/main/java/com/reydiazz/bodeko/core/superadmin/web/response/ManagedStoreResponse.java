package com.reydiazz.bodeko.core.superadmin.web.response;

import java.util.UUID;

/** Tienda asociada a una cuenta administrada. */
public record ManagedStoreResponse(UUID id, String name, String subdomain) {}
