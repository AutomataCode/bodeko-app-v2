package com.reydiazz.bodeko.core.order.history.model.entity;

import com.reydiazz.bodeko.core.order.core.model.enums.OrderStatus;
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
        name = "order_status_history",
        indexes = {
                @Index(
                        name = "idx_order_status_history_order_created",
                        columnList = "order_id, created_at"
                )
        }
)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderStatusHistory {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "order_id", nullable = false, updatable = false)
    private UUID orderId;

    @Enumerated(EnumType.STRING)
    @Column(name = "from_status", nullable = false, updatable = false)
    private OrderStatus fromStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "to_status", nullable = false, updatable = false)
    private OrderStatus toStatus;

    @Column(name = "changed_by", nullable = false, updatable = false)
    private UUID changedBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

}
