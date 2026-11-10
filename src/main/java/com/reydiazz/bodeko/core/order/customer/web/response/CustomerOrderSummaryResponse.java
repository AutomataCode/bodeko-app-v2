package com.reydiazz.bodeko.core.order.customer.web.response;

import com.reydiazz.bodeko.core.order.core.model.enums.InvoiceType;
import com.reydiazz.bodeko.core.order.core.model.enums.OrderStatus;
import com.reydiazz.bodeko.core.store.model.enums.TypeCurrency;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

/** Resumen de una compra del cliente con los datos de su tienda. */
public record CustomerOrderSummaryResponse(
        UUID id, String orderNumber, UUID storeId, String storeName, String storeSubdomain,
        TypeCurrency currency, InvoiceType invoiceType, BigDecimal total,
        OrderStatus status, OffsetDateTime createdAt
) {}
