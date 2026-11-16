package com.reydiazz.bodeko.core.billing.repository;

import com.reydiazz.bodeko.core.billing.model.BillingIssuer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/** Busca los perfiles fiscales por propietario y RUC. */
public interface BillingIssuerRepository extends JpaRepository<BillingIssuer, UUID> {
    Optional<BillingIssuer> findByOwnerIdAndTaxId(UUID ownerId, String taxId);
}
