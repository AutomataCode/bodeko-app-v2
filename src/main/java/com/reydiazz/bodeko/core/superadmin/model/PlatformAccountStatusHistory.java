package com.reydiazz.bodeko.core.superadmin.model;

import com.reydiazz.bodeko.core.user.core.model.entity.User;
import jakarta.persistence.*;
import lombok.*;
import java.time.OffsetDateTime;
import java.util.UUID;

/** Registro de auditoría de las suspensiones y reactivaciones de cuentas. */
@Entity
@Table(name = "platform_account_status_history")
@Getter @Builder @NoArgsConstructor(access = AccessLevel.PROTECTED) @AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PlatformAccountStatusHistory {
    @Id @Column(nullable = false, updatable = false) private UUID id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "actor_id", nullable = false, updatable = false) private User actor;
    @Enumerated(EnumType.STRING) @Column(name = "account_type", nullable = false, updatable = false) private PlatformAccountType accountType;
    @Column(name = "account_id", nullable = false, updatable = false) private UUID accountId;
    @Column(name = "previous_active", nullable = false, updatable = false) private boolean previousActive;
    @Column(name = "new_active", nullable = false, updatable = false) private boolean newActive;
    @Column(nullable = false, length = 500, updatable = false) private String reason;
    @Column(name = "created_at", nullable = false, updatable = false) private OffsetDateTime createdAt;
}
