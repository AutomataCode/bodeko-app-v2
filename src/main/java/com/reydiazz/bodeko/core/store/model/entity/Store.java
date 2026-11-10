package com.reydiazz.bodeko.core.store.model.entity;

import com.reydiazz.bodeko.core.store.model.enums.TypeCurrency;
import com.reydiazz.bodeko.core.user.core.model.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Builder
@Entity
@Getter
@Table(name = "stores")
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Store {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @Column(name = "name",nullable = false)
    private String name;

    @Column(name = "tax_id",nullable = false,unique = true)
    private String taxId;

    @Column(name = "legal_name",nullable = false)
    private String legalName;

    @Column(name = "subdomain",nullable = false,unique = true)
    private String subdomain;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "currency", nullable = false, length = 3)
    private TypeCurrency currency = TypeCurrency.PEN;

    @Builder.Default
    @Column(name = "tax_rate", nullable = false, precision = 4, scale = 2)
    private BigDecimal taxRate = new BigDecimal("0.18");

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    public void updateTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }

}
