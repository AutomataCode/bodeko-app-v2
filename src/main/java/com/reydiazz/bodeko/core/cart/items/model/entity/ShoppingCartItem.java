package com.reydiazz.bodeko.core.cart.items.model.entity;

import com.reydiazz.bodeko.core.product.core.model.entity.Product;
import com.reydiazz.bodeko.core.shopping.cart.model.entity.ShoppingCart;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Builder
@Entity
@Getter
@IdClass(ShoppingCartItemId.class)
@Table(name = "shopping_cart_items")
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ShoppingCartItem {

    @Id
    @Column(name = "cart_id", nullable = false, updatable = false)
    private UUID cartId;

    @Id
    @Column(name = "product_id", nullable = false, updatable = false)
    private UUID productId;

    @Column(name = "store_id", nullable = false, updatable = false)
    private UUID storeId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cart_id", referencedColumnName = "id", insertable = false, updatable = false)
    private ShoppingCart cart;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Product product;

    @Builder.Default
    @Column(name = "quantity", nullable = false)
    private Integer quantity = 1;

    public void increaseQuantity(int amount) {
        this.quantity = this.quantity + amount;
    }

    public void updateQuantity(int quantity) {
        this.quantity = quantity;
    }

}
