package com.reydiazz.bodeko.core.order.management.web.response;

import com.reydiazz.bodeko.core.order.core.model.enums.CustomerDocumentType;
import com.reydiazz.bodeko.core.order.core.model.enums.InvoiceType;
import com.reydiazz.bodeko.core.order.core.model.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record OrderSummaryResponse(
        UUID id,
        String orderNumber,
        InvoiceType invoiceType,
        CustomerDocumentType customerDocumentType,
        String customerDocumentNumber,
        String customerBusinessName,
        BigDecimal total,
        OrderStatus status,
        OffsetDateTime createdAt
) {
}
