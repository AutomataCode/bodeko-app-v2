package com.reydiazz.bodeko.core.user.core.model.entity;

import com.reydiazz.bodeko.core.user.core.model.enums.UserDocumentType;
import com.reydiazz.bodeko.core.user.core.model.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Builder
@Entity
@Getter
@Table(name = "users")
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class User {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String password;

    @Column(name = "phone_number" , length = 20)
    private String phoneNumber;

    /** Documento opcional del usuario. Cada orden conserva sus propios datos fiscales. */
    @Enumerated(EnumType.STRING)
    @Column(name = "document_type", length = 20)
    private UserDocumentType documentType;

    @Column(name = "document_number", length = 20)
    private String documentNumber;

    @Column(name = "full_name", nullable = false, length = 150)
    private String fullName;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 20)
    private UserRole role;

    @Builder.Default
    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

}
