package com.reydiazz.bodeko.core.payment.model.entity;

import com.reydiazz.bodeko.core.order.core.model.entity.Order;
import com.reydiazz.bodeko.core.payment.model.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Builder
@Entity
@Getter
@Table(
        name = "payments",
        indexes = {
                @Index(
                        name = "idx_payments_order_id",
                        columnList = "order_id"
                )
        }
)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "order_id",nullable = false)
    private Order order;

    @Column(name = "provider",nullable = false)
    private String provider;

    @Column(name = "transaction_id",nullable = false,unique = true)
    private String transactionId;

    @Column(name = "amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "status",nullable = false)
    private PaymentStatus status = PaymentStatus.PENDING;

    @Column(name = "created_at",nullable = false,updatable = false,insertable = false)
    private OffsetDateTime createdAt;

    public void markAsApproved() {
        this.status = PaymentStatus.APPROVED;
    }

    public void markAsFailed() {
        this.status = PaymentStatus.FAILED;
    }

    public void markAsRefunded() {
        this.status = PaymentStatus.REFUNDED;
    }

}
