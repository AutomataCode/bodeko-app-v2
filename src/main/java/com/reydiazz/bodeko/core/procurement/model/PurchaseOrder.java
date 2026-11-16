package com.reydiazz.bodeko.core.procurement.model;

import com.reydiazz.bodeko.core.store.model.entity.Store;
import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/** Orden interna que registra lo solicitado a un proveedor. */
@Entity
@Table(name = "purchase_orders")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PurchaseOrder {
    @Id private UUID id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "store_id", nullable = false) private Store store;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "supplier_id", nullable = false) private Supplier supplier;
    @Column(name = "order_number", nullable = false, unique = true, length = 40) private String orderNumber;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 24) private PurchaseOrderStatus status;
    @Column(length = 500) private String notes;
    @Column(name = "created_at", nullable = false, updatable = false) private OffsetDateTime createdAt;
    @Builder.Default @OneToMany(mappedBy = "purchaseOrder", cascade = CascadeType.ALL, orphanRemoval = true) private List<PurchaseOrderItem> items = new ArrayList<>();

    public void addItem(PurchaseOrderItem item) { items.add(item); }
    public void updateStatus(PurchaseOrderStatus status) { this.status = status; }
}
