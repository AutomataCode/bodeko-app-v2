package com.reydiazz.bodeko.core.order.core.web.response;

import com.reydiazz.bodeko.core.order.core.model.enums.CustomerDocumentType;
import com.reydiazz.bodeko.core.order.core.model.enums.InvoiceType;
import com.reydiazz.bodeko.core.order.core.model.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record OrderResponse(
        UUID id,
        String orderNumber,
        UUID storeId,
        InvoiceType invoiceType,
        CustomerDocumentType customerDocumentType,
        String customerDocumentNumber,
        String customerBusinessName,
        String customerBillingAddress,
        BigDecimal subtotal,
        BigDecimal taxAmount,
        BigDecimal total,
        BigDecimal appliedTaxRate,
        OrderStatus status,
        OffsetDateTime createdAt,
        List<OrderItemResponse> items
) {
}
