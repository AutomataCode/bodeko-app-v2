package com.reydiazz.bodeko.core.procurement.web;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/** Contratos validados para proveedores y órdenes de compra. */
public final class PurchaseOrderRequests {
    private PurchaseOrderRequests() {}
    public record SupplierRequest(@NotBlank @Size(max = 120) String name, @Email @Size(max = 255) String contactEmail,
                                  @Size(max = 30) String phone, @Size(max = 500) String notes) {}
    public record PurchaseOrderLineRequest(@NotNull UUID variantId, @Min(1) @Max(10000) int quantity,
                                           @NotNull @DecimalMin("0.00") @Digits(integer = 10, fraction = 2) BigDecimal unitCost) {}
    public record CreatePurchaseOrderRequest(@NotNull UUID supplierId, @NotEmpty @Size(max = 100) List<@Valid PurchaseOrderLineRequest> items,
                                             @Size(max = 500) String notes) {}
    public record PurchaseOrderReceiptLineRequest(@NotNull UUID itemId, @Min(0) @Max(10000) int accepted,
                                                  @Min(0) @Max(10000) int rejected) {}
    public record ReceivePurchaseOrderRequest(@NotEmpty @Size(max = 100) List<@Valid PurchaseOrderReceiptLineRequest> items,
                                              @NotBlank @Size(max = 300) String reason) {}
}
