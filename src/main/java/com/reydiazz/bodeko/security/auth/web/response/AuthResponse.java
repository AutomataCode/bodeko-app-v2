package com.reydiazz.bodeko.security.auth.web.response;

import com.reydiazz.bodeko.core.user.core.model.enums.UserDocumentType;
import com.reydiazz.bodeko.core.user.core.model.enums.UserRole;

import java.util.UUID;

public record AuthResponse(
        UUID id,
        String fullName,
        String email,
        UserRole role,
        String phoneNumber
) {
}
