package com.reydiazz.bodeko.core.billing.service;

import com.github.f4b6a3.uuid.UuidCreator;
import com.reydiazz.bodeko.core.billing.model.BillingIssuer;
import com.reydiazz.bodeko.core.billing.model.SalesDocument;
import com.reydiazz.bodeko.core.billing.model.SalesDocumentStatus;
import com.reydiazz.bodeko.core.billing.repository.SalesDocumentRepository;
import com.reydiazz.bodeko.core.billing.web.SalesDocumentLineResponse;
import com.reydiazz.bodeko.core.billing.web.SalesDocumentResponse;
import com.reydiazz.bodeko.core.order.core.model.entity.Order;
import com.reydiazz.bodeko.core.order.core.model.enums.OrderStatus;
import com.reydiazz.bodeko.core.order.core.repository.OrderRepository;
import com.reydiazz.bodeko.core.order.items.model.entity.OrderItem;
import com.reydiazz.bodeko.core.order.items.repository.OrderItemRepository;
import com.reydiazz.bodeko.core.store.model.entity.Store;
import com.reydiazz.bodeko.shared.exception.BusinessException;
import com.reydiazz.bodeko.shared.validation.ValidationError;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/** Emite documentos simulados una vez confirmado el pago, sin llamar a SUNAT. */
@Service
@RequiredArgsConstructor
public class SalesDocumentService {
    private final SalesDocumentRepository documentRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final JdbcTemplate jdbcTemplate;

    /** Crea el documento una sola vez o deja pendiente la emisión si falta el perfil fiscal. */
    @Transactional
    public SalesDocument issueForPaidOrder(UUID orderId) {
        Order order = orderRepository.findByIdForDocumentUpdate(orderId)
                .orElseThrow(() -> new BusinessException(ValidationError.SALES_DOCUMENT_NOT_FOUND));
        if (order.getStatus() == OrderStatus.PENDING || order.getStatus() == OrderStatus.CANCELLED)
            throw new BusinessException(ValidationError.SALES_DOCUMENT_ORDER_NOT_PAID);
        SalesDocument document = documentRepository.findByOrderId(orderId).orElseGet(() ->
                documentRepository.save(SalesDocument.builder()
                        .id(UuidCreator.getTimeOrderedEpoch()).orderId(orderId)
                        .storeId(order.getStore().getId()).issuer(order.getStore().getBillingIssuer())
                        .invoiceType(order.getInvoiceType())
                        .status(SalesDocumentStatus.PENDING_CONFIGURATION).createdAt(OffsetDateTime.now()).build()));
        if (document.getStatus() == SalesDocumentStatus.ISSUED) return document;
        BillingIssuer issuer = order.getStore().getBillingIssuer();
        if (issuer == null || issuer.getFiscalAddress() == null || issuer.getFiscalAddress().isBlank()) return document;
        String type = order.getInvoiceType().name();
        long number = jdbcTemplate.queryForObject("""
                INSERT INTO billing_document_sequences (issuer_id, invoice_type, next_number)
                VALUES (?, ?, 2)
                ON CONFLICT (issuer_id, invoice_type)
                DO UPDATE SET next_number = billing_document_sequences.next_number + 1
                RETURNING next_number - 1
                """, Long.class, issuer.getId(), type);
        String series = order.getInvoiceType() == com.reydiazz.bodeko.core.order.core.model.enums.InvoiceType.INVOICE
                ? "F001" : "B001";
        document.issue(issuer, series, number);
        return document;
    }

    /** Emite los documentos que quedaron pendientes tras completar la ficha fiscal. */
    @Transactional
    public void issuePendingForStore(UUID storeId) {
        documentRepository.findAllByStoreIdAndStatusOrderByCreatedAtAsc(storeId, SalesDocumentStatus.PENDING_CONFIGURATION)
                .forEach(document -> issueForPaidOrder(document.getOrderId()));
    }

    /** Anula el documento simulado cuando se devuelve el pago de una orden. */
    @Transactional
    public void voidForOrder(UUID orderId) {
        documentRepository.findByOrderId(orderId).ifPresent(SalesDocument::voidDocument);
    }

    /** Emite pendientes de todas las tiendas que comparten el perfil fiscal. */
    @Transactional
    public void issuePendingForIssuer(UUID issuerId) {
        if (issuerId == null) return;
        documentRepository.findAllByIssuerIdAndStatusOrderByCreatedAtAsc(issuerId, SalesDocumentStatus.PENDING_CONFIGURATION)
                .forEach(document -> issueForPaidOrder(document.getOrderId()));
    }

    /** Obtiene el documento de una orden de la tienda del administrador. */
    @Transactional
    public SalesDocumentResponse getForStore(UUID storeId, UUID orderId) {
        Order order = orderRepository.findByIdAndStoreId(orderId, storeId)
                .orElseThrow(() -> new BusinessException(ValidationError.SALES_DOCUMENT_NOT_FOUND));
        SalesDocument document = ensureIssuedOrPending(order);
        return response(order, document);
    }

    /** Obtiene el documento solo si la orden pertenece al cliente autenticado. */
    @Transactional
    public SalesDocumentResponse getForCustomer(UUID customerId, UUID orderId) {
        Order order = orderRepository.findByIdAndCustomerId(orderId, customerId)
                .orElseThrow(() -> new BusinessException(ValidationError.SALES_DOCUMENT_NOT_FOUND));
        SalesDocument document = ensureIssuedOrPending(order);
        return response(order, document);
    }

    private SalesDocument ensureIssuedOrPending(Order order) {
        if (order.getStatus() == OrderStatus.PENDING)
            throw new BusinessException(ValidationError.SALES_DOCUMENT_ORDER_NOT_PAID);
        if (order.getStatus() == OrderStatus.CANCELLED) {
            return documentRepository.findByOrderId(order.getId())
                    .filter(document -> document.getStatus() == SalesDocumentStatus.VOIDED)
                    .orElseThrow(() -> new BusinessException(ValidationError.SALES_DOCUMENT_ORDER_NOT_PAID));
        }
        return issueForPaidOrder(order.getId());
    }

    private SalesDocumentResponse response(Order order, SalesDocument document) {
        List<SalesDocumentLineResponse> items = orderItemRepository
                .findAllByOrderIdOrderByProductNameAsc(order.getId()).stream().map(this::line).toList();
        String displayNumber = document.getSeries() == null ? null
                : document.getSeries() + "-" + String.format(Locale.ROOT, "%08d", document.getDocumentNumber());
        String buyerName = order.getInvoiceType() == com.reydiazz.bodeko.core.order.core.model.enums.InvoiceType.INVOICE
                ? order.getCustomerBusinessName() : order.getCustomer().getFullName();
        return new SalesDocumentResponse(document.getId(), order.getId(), order.getOrderNumber(),
                document.getInvoiceType(), document.getStatus(), document.getSeries(), document.getDocumentNumber(),
                displayNumber, document.getIssuerTaxId(), document.getIssuerLegalName(), document.getIssuerFiscalAddress(),
                buyerName, order.getCustomerDocumentType(), order.getCustomerDocumentNumber(),
                order.getCustomerBillingAddress(), order.getStore().getCurrency(), order.getSubtotal(),
                order.getTaxAmount(), order.getAppliedTaxRate(), order.getTotal(), document.getIssuedAt(), items);
    }

    private SalesDocumentLineResponse line(OrderItem item) {
        return new SalesDocumentLineResponse(item.getProductName(), item.getVariantSku(), item.getVariantOptions(),
                item.getQuantity(), item.getChargedUnitPrice(), item.getItemSubtotal());
    }
}
