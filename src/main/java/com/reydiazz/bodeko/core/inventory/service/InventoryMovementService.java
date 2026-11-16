package com.reydiazz.bodeko.core.inventory.service;

import com.github.f4b6a3.uuid.UuidCreator;
import com.reydiazz.bodeko.core.inventory.model.entity.Inventory;
import com.reydiazz.bodeko.core.inventory.model.entity.InventoryMovement;
import com.reydiazz.bodeko.core.inventory.model.enums.InventoryMovementType;
import com.reydiazz.bodeko.core.inventory.repository.InventoryMovementRepository;
import com.reydiazz.bodeko.core.inventory.repository.InventoryRepository;
import com.reydiazz.bodeko.core.store.repository.StoreRepository;
import com.reydiazz.bodeko.core.store.exception.StoreNotFoundException;
import com.reydiazz.bodeko.core.inventory.web.response.InventoryMovementResponse;
import com.reydiazz.bodeko.core.order.items.model.entity.OrderItem;
import com.reydiazz.bodeko.shared.response.PageResponse;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/** Registra cambios de inventario y consulta movimientos dentro de una tienda. */
@Service
@RequiredArgsConstructor
public class InventoryMovementService {
    private static final int MAX_PAGE_SIZE = 100;
    private final InventoryMovementRepository movementRepository;
    private final InventoryRepository inventoryRepository;
    private final StoreRepository storeRepository;

    /** Guarda un movimiento manual usando el saldo previo y el saldo final del inventario. */
    @Transactional
    public void recordManual(Inventory inventory, InventoryMovementType type, int before, int after,
                             String reason, UUID actorId, String actorName, String actorEmail) {
        if (before == after) return;
        save(inventory, type, after - before, before, after, "MANUAL", null, null,
                reason, actorId, actorName, actorEmail, "ADMIN");
    }

    /** Registra un evento de orden con el stock que existe al ocurrir el evento. */
    @Transactional
    public void recordOrderItem(OrderItem item, InventoryMovementType type, int quantityChange,
                                String reason, UUID actorId, String actorName, String actorEmail, String actorRole) {
        Inventory inventory = inventoryRepository.findByVariantId(item.getVariantId())
                .orElseThrow(() -> new IllegalStateException("No se encontró el inventario de la variante"));
        int after = inventory.getQuantity();
        int before = after - quantityChange;
        String options = item.getVariantOptions().entrySet().stream()
                .sorted(Map.Entry.comparingByKey(String.CASE_INSENSITIVE_ORDER))
                .map(entry -> entry.getKey() + ": " + entry.getValue())
                .reduce((left, right) -> left + ", " + right).orElse("Presentación estándar");
        save(inventory, type, quantityChange, before, after, "ORDER", item.getOrderId(),
                item.getOrder().getOrderNumber(), reason, actorId, actorName, actorEmail, actorRole,
                item.getProductName(), item.getVariantSku(), options, item.getQuantity());
    }

    /** Devuelve el historial más reciente con filtros limitados a la tienda solicitada. */
    @Transactional(readOnly = true)
    public PageResponse<InventoryMovementResponse> search(UUID storeId, String search, InventoryMovementType type,
                                                          String actor, String reference, OffsetDateTime from,
                                                          OffsetDateTime to, int page, int size) {
        return searchInternal(storeId, search, type, actor, reference, from, to, page, size, false);
    }

    /** Consulta solo daños y pérdidas registrados como merma. */
    @Transactional(readOnly = true)
    public PageResponse<InventoryMovementResponse> searchShrinkages(UUID storeId, UUID actorId, String search, InventoryMovementType type,
                                                                     String actor, String reference, OffsetDateTime from,
                                                                     OffsetDateTime to, int page, int size) {
        if (!storeRepository.existsByIdAndOwnerId(storeId, actorId)) throw new StoreNotFoundException();
        if (type != null && type != InventoryMovementType.DAMAGE && type != InventoryMovementType.LOSS) {
            throw new com.reydiazz.bodeko.shared.exception.BusinessException(
                    com.reydiazz.bodeko.shared.validation.ValidationError.INVALID_SHRINKAGE_TYPE);
        }
        return searchInternal(storeId, search, type, actor, reference, from, to, page, size, true);
    }

    private PageResponse<InventoryMovementResponse> searchInternal(UUID storeId, String search, InventoryMovementType type,
                                                          String actor, String reference, OffsetDateTime from,
                                                          OffsetDateTime to, int page, int size, boolean shrinkagesOnly) {
        int safePage = Math.max(page, 0);
        int safeSize = Math.min(Math.max(size, 1), MAX_PAGE_SIZE);
        Specification<InventoryMovement> specification = (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(builder.equal(root.get("storeId"), storeId));
            if (type != null) predicates.add(builder.equal(root.get("movementType"), type));
            else if (shrinkagesOnly) predicates.add(root.get("movementType").in(InventoryMovementType.DAMAGE, InventoryMovementType.LOSS));
            if (from != null) predicates.add(builder.greaterThanOrEqualTo(root.get("createdAt"), from));
            if (to != null) predicates.add(builder.lessThanOrEqualTo(root.get("createdAt"), to));
            if (search != null && !search.isBlank()) {
                String value = "%" + search.trim().toLowerCase() + "%";
                predicates.add(builder.or(
                        builder.like(builder.lower(root.get("productName")), value),
                        builder.like(builder.lower(root.get("variantSku")), value),
                        builder.like(builder.lower(root.get("variantOptions")), value)));
            }
            if (actor != null && !actor.isBlank()) {
                String value = "%" + actor.trim().toLowerCase() + "%";
                predicates.add(builder.or(
                        builder.like(builder.lower(root.get("actorName")), value),
                        builder.like(builder.lower(root.get("actorEmail")), value)));
            }
            if (reference != null && !reference.isBlank()) {
                String value = "%" + reference.trim().toLowerCase() + "%";
                predicates.add(builder.or(
                        builder.like(builder.lower(root.get("referenceNumber")), value),
                        builder.like(builder.lower(root.get("reason")), value)));
            }
            return builder.and(predicates.toArray(Predicate[]::new));
        };
        Page<InventoryMovement> results = movementRepository.findAll(specification,
                PageRequest.of(safePage, safeSize, Sort.by(Sort.Direction.DESC, "createdAt")));
        return new PageResponse<>(results.getContent().stream().map(this::toResponse).toList(),
                results.getNumber(), results.getSize(), results.getTotalElements(), results.getTotalPages());
    }

    private void save(Inventory inventory, InventoryMovementType type, int delta, int before, int after,
                      String referenceType, UUID referenceId, String referenceNumber, String reason,
                      UUID actorId, String actorName, String actorEmail, String actorRole) {
        save(inventory, type, delta, before, after, referenceType, referenceId, referenceNumber,
                reason, actorId, actorName, actorEmail, actorRole,
                inventory.getProduct().getName(), inventory.getVariant().getSku(),
                inventory.getVariant().getOptions().entrySet().stream()
                        .sorted(Map.Entry.comparingByKey(String.CASE_INSENSITIVE_ORDER))
                        .map(entry -> entry.getKey() + ": " + entry.getValue())
                        .reduce((left, right) -> left + ", " + right).orElse("Presentación estándar"), Math.abs(delta));
    }

    private void save(Inventory inventory, InventoryMovementType type, int delta, int before, int after,
                      String referenceType, UUID referenceId, String referenceNumber, String reason,
                      UUID actorId, String actorName, String actorEmail, String actorRole,
                      String productName, String sku, String options, int units) {
        movementRepository.save(InventoryMovement.builder()
                .id(UuidCreator.getTimeOrderedEpoch())
                .storeId(inventory.getProduct().getStore().getId())
                .productId(inventory.getProduct().getId())
                .variantId(inventory.getVariantId())
                .productName(productName)
                .variantSku(sku)
                .variantOptions(options)
                .movementType(type)
                .units(units)
                .quantityChange(delta)
                .quantityBefore(before)
                .quantityAfter(after)
                .referenceType(referenceType)
                .referenceId(referenceId)
                .referenceNumber(referenceNumber)
                .reason(reason)
                .actorId(actorId)
                .actorName(actorName)
                .actorEmail(actorEmail)
                .actorRole(actorRole)
                .createdAt(OffsetDateTime.now())
                .build());
    }

    private InventoryMovementResponse toResponse(InventoryMovement movement) {
        return new InventoryMovementResponse(movement.getId(), movement.getProductId(), movement.getVariantId(),
                movement.getProductName(), movement.getVariantSku(), movement.getVariantOptions(),
                movement.getMovementType(), movement.getUnits(), movement.getQuantityChange(), movement.getQuantityBefore(),
                movement.getQuantityAfter(), movement.getReferenceType(), movement.getReferenceId(),
                movement.getReferenceNumber(), movement.getReason(), movement.getActorId(),
                movement.getActorName(), movement.getActorEmail(), movement.getActorRole(), movement.getCreatedAt());
    }
}
