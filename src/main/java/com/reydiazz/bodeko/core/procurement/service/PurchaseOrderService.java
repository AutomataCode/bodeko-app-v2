package com.reydiazz.bodeko.core.procurement.service;

import com.github.f4b6a3.uuid.UuidCreator;
import com.reydiazz.bodeko.core.inventory.model.entity.Inventory;
import com.reydiazz.bodeko.core.inventory.model.enums.InventoryMovementType;
import com.reydiazz.bodeko.core.inventory.service.InventoryMovementService;
import com.reydiazz.bodeko.core.inventory.service.InventoryService;
import com.reydiazz.bodeko.core.procurement.model.*;
import com.reydiazz.bodeko.core.procurement.repository.*;
import com.reydiazz.bodeko.core.procurement.web.*;
import com.reydiazz.bodeko.core.procurement.web.PurchaseOrderRequests.*;
import com.reydiazz.bodeko.core.procurement.web.PurchaseOrderResponses.*;
import com.reydiazz.bodeko.core.store.exception.StoreNotFoundException;
import com.reydiazz.bodeko.core.store.repository.StoreRepository;
import com.reydiazz.bodeko.core.product.variant.repository.ProductVariantRepository;
import com.reydiazz.bodeko.shared.exception.BusinessException;
import com.reydiazz.bodeko.shared.validation.ValidationError;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

/** Gestiona proveedores y órdenes de compra sin emitir comprobantes fiscales. */
@Service
@RequiredArgsConstructor
public class PurchaseOrderService {
    private final SupplierRepository suppliers;
    private final PurchaseOrderRepository orders;
    private final StoreRepository stores;
    private final ProductVariantRepository variants;
    private final InventoryService inventory;
    private final InventoryMovementService movements;

    @Transactional(readOnly = true)
    public List<SupplierResponse> listSuppliers(UUID storeId, UUID ownerId) {
        authorize(storeId, ownerId);
        return suppliers.findAllByStoreIdAndActiveTrueOrderByName(storeId).stream().map(this::supplierResponse).toList();
    }

    @Transactional
    public SupplierResponse createSupplier(UUID storeId, UUID ownerId, SupplierRequest request) {
        authorize(storeId, ownerId);
        String name = request.name().trim().replaceAll("\\s+", " ");
        if (suppliers.existsByStoreIdAndNameIgnoreCase(storeId, name)) throw new BusinessException(ValidationError.SUPPLIER_DUPLICATE);
        try {
            Supplier supplier = suppliers.saveAndFlush(Supplier.builder().id(UuidCreator.getTimeOrderedEpoch())
                    .store(stores.getReferenceById(storeId)).name(name)
                    .contactEmail(clean(request.contactEmail())).phone(clean(request.phone())).notes(clean(request.notes()))
                    .active(true).createdAt(OffsetDateTime.now()).build());
            return supplierResponse(supplier);
        } catch (DataIntegrityViolationException duplicate) {
            throw new BusinessException(ValidationError.SUPPLIER_DUPLICATE);
        }
    }

    @Transactional(readOnly = true)
    public List<PurchaseOrderResponse> list(UUID storeId, UUID ownerId) {
        authorize(storeId, ownerId);
        return orders.findRecentByStore(storeId).stream().limit(100).map(this::orderResponse).toList();
    }

    @Transactional
    public PurchaseOrderResponse create(UUID storeId, UUID ownerId, CreatePurchaseOrderRequest request) {
        authorize(storeId, ownerId);
        Supplier supplier = suppliers.findByIdAndStoreIdAndActiveTrue(request.supplierId(), storeId)
                .orElseThrow(() -> new BusinessException(ValidationError.SUPPLIER_NOT_FOUND));
        if (request.items().stream().map(PurchaseOrderLineRequest::variantId).distinct().count() != request.items().size())
            throw new BusinessException(ValidationError.PURCHASE_ORDER_DUPLICATE_VARIANT);
        PurchaseOrder order = PurchaseOrder.builder().id(UuidCreator.getTimeOrderedEpoch()).store(stores.getReferenceById(storeId))
                .supplier(supplier).orderNumber("OC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase(Locale.ROOT))
                .status(PurchaseOrderStatus.ORDERED).notes(clean(request.notes())).createdAt(OffsetDateTime.now()).build();
        for (PurchaseOrderLineRequest line : request.items()) {
            var variant = variants.findByIdAndStoreIdForUpdate(line.variantId(), storeId)
                    .orElseThrow(() -> new BusinessException(ValidationError.PURCHASE_ORDER_VARIANT_NOT_FOUND));
            String options = variant.getOptions().entrySet().stream().sorted(Map.Entry.comparingByKey(String.CASE_INSENSITIVE_ORDER))
                    .map(entry -> entry.getKey() + ": " + entry.getValue()).collect(Collectors.joining(", "));
            options = options.substring(0, Math.min(options.length(), 500));
            if (!variant.isActive() || !variant.getProduct().getStatus().name().equals("ACTIVE"))
                throw new BusinessException(ValidationError.PURCHASE_ORDER_VARIANT_NOT_FOUND);
            order.addItem(PurchaseOrderItem.builder().id(UuidCreator.getTimeOrderedEpoch()).purchaseOrder(order)
                    .variantId(variant.getId()).productName(variant.getProduct().getName()).sku(variant.getSku())
                    .variantOptions(options).quantityOrdered(line.quantity()).unitCost(line.unitCost()).build());
        }
        return orderResponse(orders.save(order));
    }

    @Transactional
    public PurchaseOrderResponse receive(UUID storeId, UUID ownerId, String actorName, String actorEmail, UUID orderId, ReceivePurchaseOrderRequest request) {
        authorize(storeId, ownerId);
        PurchaseOrder order = orders.findByIdAndStoreIdForUpdate(orderId, storeId)
                .orElseThrow(() -> new BusinessException(ValidationError.PURCHASE_ORDER_NOT_FOUND));
        if (order.getStatus() != PurchaseOrderStatus.ORDERED && order.getStatus() != PurchaseOrderStatus.PARTIALLY_RECEIVED)
            throw new BusinessException(ValidationError.PURCHASE_ORDER_NOT_RECEIVABLE);
        Set<UUID> duplicateIds = new HashSet<>();
        List<PurchaseOrderReceiptLineRequest> lines = request.items().stream()
                .sorted(Comparator.comparing(PurchaseOrderReceiptLineRequest::itemId)).toList();
        for (var line : lines) {
            if (!duplicateIds.add(line.itemId()) || line.accepted() + line.rejected() == 0)
                throw new BusinessException(ValidationError.PURCHASE_ORDER_INVALID_RECEIPT);
            PurchaseOrderItem item = order.getItems().stream().filter(candidate -> candidate.getId().equals(line.itemId())).findFirst()
                    .orElseThrow(() -> new BusinessException(ValidationError.PURCHASE_ORDER_ITEM_NOT_FOUND));
            if (line.accepted() + line.rejected() > item.getPendingQuantity())
                throw new BusinessException(ValidationError.PURCHASE_ORDER_INVALID_RECEIPT);
            if (line.accepted() > 0) {
                Inventory stock = inventory.lockVariantStock(item.getVariantId());
                if (!stock.getProduct().getStore().getId().equals(storeId)) throw new BusinessException(ValidationError.PURCHASE_ORDER_VARIANT_NOT_FOUND);
                int before = stock.getQuantity();
                stock.increaseQuantity(line.accepted());
                movements.recordManual(stock, InventoryMovementType.STOCK_ENTRY, before, stock.getQuantity(),
                        "Recepción " + order.getOrderNumber() + ": " + request.reason().trim(), ownerId, actorName, actorEmail);
            }
            item.receive(line.accepted(), line.rejected());
        }
        boolean complete = order.getItems().stream().allMatch(item -> item.getPendingQuantity() == 0);
        boolean started = order.getItems().stream().anyMatch(item -> item.getQuantityReceived() + item.getQuantityRejected() > 0);
        order.updateStatus(complete ? PurchaseOrderStatus.RECEIVED : started ? PurchaseOrderStatus.PARTIALLY_RECEIVED : PurchaseOrderStatus.ORDERED);
        return orderResponse(order);
    }

    private void authorize(UUID storeId, UUID ownerId) { if (!stores.existsByIdAndOwnerId(storeId, ownerId)) throw new StoreNotFoundException(); }
    private String clean(String value) { return value == null || value.isBlank() ? null : value.trim(); }
    private SupplierResponse supplierResponse(Supplier supplier) { return new SupplierResponse(supplier.getId(), supplier.getName(), supplier.getContactEmail(), supplier.getPhone(), supplier.getNotes()); }
    private PurchaseOrderResponse orderResponse(PurchaseOrder order) {
        return new PurchaseOrderResponse(order.getId(), order.getOrderNumber(), order.getSupplier().getId(), order.getSupplier().getName(), order.getStatus(), order.getNotes(), order.getCreatedAt(),
                order.getItems().stream().map(item -> new PurchaseOrderItemResponse(item.getId(), item.getVariantId(), item.getProductName(), item.getSku(), item.getVariantOptions(), item.getQuantityOrdered(), item.getQuantityReceived(), item.getQuantityRejected(), item.getPendingQuantity(), item.getUnitCost())).toList());
    }
}
