package com.reydiazz.bodeko.core.billing.model;

import com.reydiazz.bodeko.core.order.core.model.enums.InvoiceType;
import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

/** Registro inmutable de una boleta o factura simulada asociada a una venta pagada. */
@Entity
@Table(name = "sales_documents", uniqueConstraints = @UniqueConstraint(
        name = "uq_sales_documents_order", columnNames = "order_id"), indexes = {
        @Index(name = "idx_sales_documents_store_created", columnList = "store_id, created_at")
})
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SalesDocument {
    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(name = "order_id", nullable = false, updatable = false)
    private UUID orderId;

    @Column(name = "store_id", nullable = false, updatable = false)
    private UUID storeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "billing_issuer_id", updatable = false)
    private BillingIssuer issuer;

    @Enumerated(EnumType.STRING)
    @Column(name = "invoice_type", nullable = false, length = 20, updatable = false)
    private InvoiceType invoiceType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 32)
    private SalesDocumentStatus status;

    @Column(name = "series", length = 4, updatable = false)
    private String series;

    @Column(name = "document_number", updatable = false)
    private Long documentNumber;

    @Column(name = "issuer_tax_id", length = 11, updatable = false)
    private String issuerTaxId;

    @Column(name = "issuer_legal_name", length = 200, updatable = false)
    private String issuerLegalName;

    @Column(name = "issuer_fiscal_address", length = 300, updatable = false)
    private String issuerFiscalAddress;

    @Column(name = "issued_at", updatable = false)
    private OffsetDateTime issuedAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    /** Completa una emisión pendiente después de registrar los datos fiscales. */
    /** Marca como anulado el documento simulado si se reembolsa la venta. */
    public void voidDocument() {
        this.status = SalesDocumentStatus.VOIDED;
    }

    public void issue(BillingIssuer issuer, String series, long number) {
        this.issuer = issuer;
        this.issuerTaxId = issuer.getTaxId();
        this.issuerLegalName = issuer.getLegalName();
        this.issuerFiscalAddress = issuer.getFiscalAddress();
        this.series = series;
        this.documentNumber = number;
        this.issuedAt = OffsetDateTime.now();
        this.status = SalesDocumentStatus.ISSUED;
    }
}
