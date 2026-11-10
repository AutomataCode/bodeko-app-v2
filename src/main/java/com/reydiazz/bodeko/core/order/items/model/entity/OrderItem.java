package com.reydiazz.bodeko.core.order.items.model.entity;

import com.reydiazz.bodeko.core.order.core.model.entity.Order;
import com.reydiazz.bodeko.core.product.core.model.entity.Product;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
@Entity
@Getter
@IdClass(OrderItemId.class)
@Table(name = "order_items")
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderItem {

    @Id
    @Column(name = "order_id", nullable = false, updatable = false)
    private UUID orderId;

    @Id
    @Column(name = "product_id", nullable = false, updatable = false)
    private UUID productId;

    @Column(name = "store_id", nullable = false, updatable = false)
    private UUID storeId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Product product;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "list_unit_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal listUnitPrice;

    @Column(name = "charged_unit_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal chargedUnitPrice;

    @Column(name = "item_subtotal", nullable = false, precision = 12, scale = 2)
    private BigDecimal itemSubtotal;
}
