package com.reydiazz.bodeko.core.superadmin.web.response;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

/** Datos necesarios para listar una cuenta sin exponer credenciales. */
public record ManagedAccountResponse(UUID id, String fullName, String email, String phoneNumber,
                                     String role, boolean active, OffsetDateTime createdAt,
                                     List<ManagedStoreResponse> stores) {}
