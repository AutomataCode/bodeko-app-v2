package com.reydiazz.bodeko.core.billing.model;

import com.reydiazz.bodeko.core.user.core.model.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

/** Perfil fiscal reutilizable por las tiendas de un mismo propietario. */
@Entity
@Table(name = "billing_issuers", uniqueConstraints = @UniqueConstraint(
        name = "uq_billing_issuer_owner_tax_id", columnNames = {"owner_id", "tax_id"}))
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BillingIssuer {
    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_id", nullable = false, updatable = false)
    private User owner;

    @Column(name = "tax_id", nullable = false, length = 11)
    private String taxId;

    @Column(name = "legal_name", nullable = false, length = 200)
    private String legalName;

    @Column(name = "fiscal_address", length = 300)
    private String fiscalAddress;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    /** Actualiza los datos compartidos por todas las tiendas asociadas. */
    public void update(String taxId, String legalName, String fiscalAddress) {
        this.taxId = taxId;
        this.legalName = legalName;
        this.fiscalAddress = fiscalAddress;
        this.updatedAt = OffsetDateTime.now();
    }
}
