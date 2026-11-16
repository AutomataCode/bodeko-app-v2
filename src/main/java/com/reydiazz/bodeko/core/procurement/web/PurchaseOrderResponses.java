package com.reydiazz.bodeko.core.procurement.web;

import com.reydiazz.bodeko.core.procurement.model.PurchaseOrderStatus;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

/** Respuestas públicas del módulo de compras. */
public final class PurchaseOrderResponses {
    private PurchaseOrderResponses() {}
    public record SupplierResponse(UUID id, String name, String contactEmail, String phone, String notes) {}
    public record PurchaseOrderItemResponse(UUID id, UUID variantId, String productName, String sku, String variantOptions,
                                            int quantityOrdered, int quantityReceived, int quantityRejected,
                                            int quantityPending, BigDecimal unitCost) {}
    public record PurchaseOrderResponse(UUID id, String orderNumber, UUID supplierId, String supplierName,
                                        PurchaseOrderStatus status, String notes, OffsetDateTime createdAt,
                                        List<PurchaseOrderItemResponse> items) {}
}
