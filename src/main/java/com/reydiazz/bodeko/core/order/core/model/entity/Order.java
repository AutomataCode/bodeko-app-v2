package com.reydiazz.bodeko.core.order.core.model.entity;

import com.reydiazz.bodeko.core.order.core.model.enums.CustomerDocumentType;
import com.reydiazz.bodeko.core.order.core.model.enums.InvoiceType;
import com.reydiazz.bodeko.core.order.core.model.enums.OrderStatus;
import com.reydiazz.bodeko.core.store.model.entity.Store;
import com.reydiazz.bodeko.core.user.core.model.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Builder
@Entity
@Getter
@Table(
        name = "orders",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uq_orders_id_store",
                        columnNames = {"id", "store_id"}
                )
        },
        indexes = {
                @Index(
                        name = "idx_orders_store_status",
                        columnList = "store_id, status"
                ),
                @Index(
                        name = "idx_orders_customer_id",
                        columnList = "customer_id"
                ),
                @Index(
                        name = "idx_orders_created_at",
                        columnList = "created_at"
                )
        }
)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "store_id",nullable = false)
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id",nullable = false)
    private User customer;

    @Column(name = "order_number",nullable = false,unique = true)
    private String orderNumber;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "invoice_type",nullable = false)
    private InvoiceType invoiceType = InvoiceType.RECEIPT;

    @Enumerated(EnumType.STRING)
    @Column(name = "customer_document_type", nullable = false)
    private CustomerDocumentType customerDocumentType;

    @Column(name = "customer_document_number", nullable = false)
    private String customerDocumentNumber;

    @Column(name = "customer_business_name")
    private String customerBusinessName;

    @Column(name = "customer_billing_address")
    private String customerBillingAddress;

    @Column(name = "subtotal", nullable = false, precision = 12, scale = 2)
    private BigDecimal subtotal;

    @Column(name = "tax_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal taxAmount;

    @Column(name = "total", nullable = false, precision = 12, scale = 2)
    private BigDecimal total;

    @Column(name = "applied_tax_rate", nullable = false, precision = 4, scale = 2)
    private BigDecimal appliedTaxRate;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderStatus status = OrderStatus.PENDING;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    public void markAsPaid() {
        this.status = OrderStatus.PAID;
    }

    public void changeStatus(OrderStatus status) {
        this.status = status;
    }

}
