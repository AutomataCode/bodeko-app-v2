package com.reydiazz.bodeko.security.auth.web.response;

import com.reydiazz.bodeko.core.user.core.model.enums.UserRole;

import java.util.UUID;

/** storeId solo se informa para clientes: es la tienda donde está registrada la cuenta. */
public record AuthResponse(
        UUID id,
        String fullName,
        String email,
        UserRole role,
        String phoneNumber,
        UUID storeId
) {
}
