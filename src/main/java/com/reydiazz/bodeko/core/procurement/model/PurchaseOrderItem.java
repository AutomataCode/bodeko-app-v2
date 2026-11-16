package com.reydiazz.bodeko.core.procurement.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.UUID;

/** Variante y cantidades acordadas/recibidas en una orden de compra. */
@Entity
@Table(name = "purchase_order_items")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PurchaseOrderItem {
    @Id private UUID id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "purchase_order_id", nullable = false) private PurchaseOrder purchaseOrder;
    @Column(name = "variant_id", nullable = false, updatable = false) private UUID variantId;
    @Column(name = "product_name", nullable = false, length = 200) private String productName;
    @Column(nullable = false, length = 80) private String sku;
    @Column(name = "variant_options", nullable = false, length = 500) private String variantOptions;
    @Column(name = "quantity_ordered", nullable = false) private int quantityOrdered;
    @Builder.Default @Column(name = "quantity_received", nullable = false) private int quantityReceived = 0;
    @Builder.Default @Column(name = "quantity_rejected", nullable = false) private int quantityRejected = 0;
    @Column(name = "unit_cost", nullable = false, precision = 12, scale = 2) private BigDecimal unitCost;

    public int getPendingQuantity() { return quantityOrdered - quantityReceived - quantityRejected; }
    public void receive(int accepted, int rejected) { quantityReceived += accepted; quantityRejected += rejected; }
}
