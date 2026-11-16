package com.reydiazz.bodeko.core.customer.model.entity;

import com.reydiazz.bodeko.core.store.model.entity.Store;
import com.reydiazz.bodeko.core.user.core.model.enums.UserDocumentType;
import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

/** Cliente registrado en una tienda concreta; no tiene acceso a otras tiendas. */
@Builder
@Entity
@Getter
@Table(name = "customers")
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Customer {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "store_id", nullable = false, updatable = false)
    private Store store;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String password;

    @Column(name = "full_name", nullable = false, length = 150)
    private String fullName;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "document_type", length = 20)
    private UserDocumentType documentType;

    @Column(name = "document_number", length = 20)
    private String documentNumber;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Builder.Default
    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    @Builder.Default
    @Column(name = "marketing_consent", nullable = false)
    private boolean marketingConsent = false;

    /** Identificador de la tienda sin inicializar la relación perezosa. */
    public UUID getStoreId() {
        return store.getId();
    }

    /** Suspende o reactiva la cuenta sin borrar órdenes ni carrito. */
    public void changeActive(boolean active) { this.isActive = active; }

    /** Actualiza el permiso opcional para recibir promociones. */
    public void changeMarketingConsent(boolean enabled) { this.marketingConsent = enabled; }

}
