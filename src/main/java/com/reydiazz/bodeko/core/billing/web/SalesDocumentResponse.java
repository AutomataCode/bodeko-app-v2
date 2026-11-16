package com.reydiazz.bodeko.core.billing.web;

import com.reydiazz.bodeko.core.billing.model.SalesDocumentStatus;
import com.reydiazz.bodeko.core.order.core.model.enums.CustomerDocumentType;
import com.reydiazz.bodeko.core.order.core.model.enums.InvoiceType;
import com.reydiazz.bodeko.core.store.model.enums.TypeCurrency;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

/** Datos necesarios para consultar e imprimir una boleta o factura simulada. */
public record SalesDocumentResponse(
        UUID id, UUID orderId, String orderNumber, InvoiceType invoiceType,
        SalesDocumentStatus status, String series, Long documentNumber, String displayNumber,
        String issuerTaxId, String issuerLegalName, String issuerFiscalAddress,
        String buyerName, CustomerDocumentType buyerDocumentType, String buyerDocumentNumber,
        String buyerAddress, TypeCurrency currency, BigDecimal subtotal, BigDecimal taxAmount,
        BigDecimal taxRate, BigDecimal total, OffsetDateTime issuedAt,
        List<SalesDocumentLineResponse> items
) {}
