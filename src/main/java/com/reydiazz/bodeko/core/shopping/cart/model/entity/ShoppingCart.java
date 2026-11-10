package com.reydiazz.bodeko.core.shopping.cart.model.entity;

import com.reydiazz.bodeko.core.shopping.cart.model.enums.ShoppingCartStatus;
import com.reydiazz.bodeko.core.store.model.entity.Store;
import com.reydiazz.bodeko.core.user.core.model.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@Builder
@Entity
@Getter
@Table(
        name = "shopping_carts",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uq_shopping_carts_id_store",
                        columnNames = {"id", "store_id"}
                )
        },
        indexes = {
                @Index(
                        name = "idx_shopping_carts_user_store",
                        columnList = "user_id, store_id"
                ),
                @Index(
                        name = "idx_shopping_carts_store_id",
                        columnList = "store_id"
                )
        }
)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ShoppingCart {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "store_id",nullable = false)
    private Store store;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "status",nullable = false)
    private ShoppingCartStatus status = ShoppingCartStatus.OPEN;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    public void markAsConverted() {
        this.status = ShoppingCartStatus.CONVERTED;
    }


}
