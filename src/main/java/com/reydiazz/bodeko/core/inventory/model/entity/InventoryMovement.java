package com.reydiazz.bodeko.core.inventory.model.entity;

import com.reydiazz.bodeko.core.inventory.model.enums.InventoryMovementType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

/** Registro inmutable de una reserva, venta, entrada o ajuste de stock. */
@Entity
@Table(name = "inventory_movements", indexes = {
        @Index(name = "idx_inventory_movements_store_date", columnList = "store_id, created_at"),
        @Index(name = "idx_inventory_movements_store_type_date", columnList = "store_id, movement_type, created_at"),
        @Index(name = "idx_inventory_movements_variant_date", columnList = "variant_id, created_at")
})
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class InventoryMovement {
    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(name = "store_id", nullable = false, updatable = false)
    private UUID storeId;

    @Column(name = "product_id", nullable = false, updatable = false)
    private UUID productId;

    @Column(name = "variant_id", nullable = false, updatable = false)
    private UUID variantId;

    @Column(name = "product_name", nullable = false, length = 200, updatable = false)
    private String productName;

    @Column(name = "variant_sku", nullable = false, length = 80, updatable = false)
    private String variantSku;

    @Column(name = "variant_options", nullable = false, length = 2000, updatable = false)
    private String variantOptions;

    @Enumerated(EnumType.STRING)
    @Column(name = "movement_type", nullable = false, length = 32, updatable = false)
    private InventoryMovementType movementType;

    /** Cambio neto de unidades disponibles; en la venta vale cero porque se descontó al reservar. */
    @Column(name = "units", nullable = false, updatable = false)
    private int units;

    @Column(name = "quantity_change", nullable = false, updatable = false)
    private int quantityChange;

    @Column(name = "quantity_before", nullable = false, updatable = false)
    private int quantityBefore;

    @Column(name = "quantity_after", nullable = false, updatable = false)
    private int quantityAfter;

    @Column(name = "reference_type", length = 30, updatable = false)
    private String referenceType;

    @Column(name = "reference_id", updatable = false)
    private UUID referenceId;

    @Column(name = "reference_number", length = 100, updatable = false)
    private String referenceNumber;

    @Column(name = "reason", length = 500, updatable = false)
    private String reason;

    @Column(name = "actor_id", updatable = false)
    private UUID actorId;

    @Column(name = "actor_name", length = 150, updatable = false)
    private String actorName;

    @Column(name = "actor_email", length = 255, updatable = false)
    private String actorEmail;

    @Column(name = "actor_role", length = 30, updatable = false)
    private String actorRole;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;
}
