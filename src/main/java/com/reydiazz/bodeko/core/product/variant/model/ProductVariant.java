package com.reydiazz.bodeko.core.product.variant.model;

import com.reydiazz.bodeko.core.product.core.model.entity.Product;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

/** Unidad vendible con SKU, opciones, precio y stock propios. */
@Entity
@Table(name = "product_variants")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductVariant {
    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumns({
            @JoinColumn(name = "product_id", referencedColumnName = "id", insertable = false, updatable = false),
            @JoinColumn(name = "store_id", referencedColumnName = "store_id", insertable = false, updatable = false)
    })
    private Product product;

    @Column(name = "product_id", nullable = false, updatable = false)
    private UUID productId;

    @Column(name = "store_id", nullable = false, updatable = false)
    private UUID storeId;

    @Column(nullable = false, length = 80)
    private String sku;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false, columnDefinition = "jsonb")
    private Map<String, String> options;

    @Column(name = "options_key", nullable = false, length = 2000)
    private String optionsKey;

    @Column(name = "base_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal basePrice;

    @Column(name = "is_default", nullable = false, updatable = false)
    private boolean isDefault;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    /** Activa o pausa la venta de esta combinación sin eliminar sus referencias. */
    public void changeActive(boolean active) {
        this.isActive = active;
    }

    /** Mantiene actualizado el precio inicial del producto, que representa la variante estándar. */
    public void updateBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }

    /** Actualiza los datos comerciales sin cambiar la identidad de la variante. */
    public void updateDetails(String sku, Map<String, String> options, String optionsKey, BigDecimal basePrice) {
        this.sku = sku;
        this.options = options;
        this.optionsKey = optionsKey;
        this.basePrice = basePrice;
    }
}
