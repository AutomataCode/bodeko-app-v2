package com.reydiazz.bodeko.core.procurement.model;

import com.reydiazz.bodeko.core.store.model.entity.Store;
import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

/** Proveedor registrado dentro de una tienda. */
@Entity
@Table(name = "suppliers")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Supplier {
    @Id private UUID id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "store_id", nullable = false) private Store store;
    @Column(nullable = false, length = 120) private String name;
    @Column(name = "contact_email", length = 255) private String contactEmail;
    @Column(length = 30) private String phone;
    @Column(length = 500) private String notes;
    @Builder.Default @Column(nullable = false) private boolean active = true;
    @Column(name = "created_at", nullable = false, updatable = false) private OffsetDateTime createdAt;
}
