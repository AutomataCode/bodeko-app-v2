package com.reydiazz.bodeko.core.billing.repository;

import com.reydiazz.bodeko.core.billing.model.SalesDocument;
import com.reydiazz.bodeko.core.billing.model.SalesDocumentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/** Acceso idempotente a los documentos por pedido y tienda. */
public interface SalesDocumentRepository extends JpaRepository<SalesDocument, UUID> {
    Optional<SalesDocument> findByOrderId(UUID orderId);
    Optional<SalesDocument> findByOrderIdAndStoreId(UUID orderId, UUID storeId);
    List<SalesDocument> findAllByStoreIdAndStatusOrderByCreatedAtAsc(UUID storeId, SalesDocumentStatus status);
    List<SalesDocument> findAllByIssuerIdAndStatusOrderByCreatedAtAsc(UUID issuerId, SalesDocumentStatus status);
}
