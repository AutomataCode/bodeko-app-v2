package com.reydiazz.bodeko.core.billing.service;

import com.github.f4b6a3.uuid.UuidCreator;
import com.reydiazz.bodeko.core.billing.model.BillingIssuer;
import com.reydiazz.bodeko.core.billing.repository.BillingIssuerRepository;
import com.reydiazz.bodeko.core.billing.web.BillingProfileRequest;
import com.reydiazz.bodeko.core.billing.web.BillingProfileResponse;
import com.reydiazz.bodeko.core.store.model.entity.Store;
import com.reydiazz.bodeko.core.store.repository.StoreRepository;
import com.reydiazz.bodeko.core.user.core.repository.UserRepository;
import com.reydiazz.bodeko.shared.exception.BusinessException;
import com.reydiazz.bodeko.shared.validation.ValidationError;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.UUID;

/** Crea y administra perfiles fiscales compartidos entre tiendas del propietario. */
@Service
@RequiredArgsConstructor
public class BillingIssuerService {
    private final BillingIssuerRepository issuerRepository;
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;

    /** Obtiene un perfil existente del mismo propietario o crea uno incompleto. */
    @Transactional
    public BillingIssuer getOrCreate(UUID ownerId, String taxId, String legalName) {
        return issuerRepository.findByOwnerIdAndTaxId(ownerId, taxId)
                .map(issuer -> {
                    if (!issuer.getLegalName().equalsIgnoreCase(legalName.trim()))
                        throw new BusinessException(ValidationError.BILLING_PROFILE_CONFLICT);
                    return issuer;
                }).orElseGet(() -> issuerRepository.save(BillingIssuer.builder()
                        .id(UuidCreator.getTimeOrderedEpoch())
                        .owner(userRepository.getReferenceById(ownerId))
                        .taxId(taxId)
                        .legalName(legalName.trim())
                        .createdAt(OffsetDateTime.now())
                        .updatedAt(OffsetDateTime.now())
                        .build()));
    }

    /** Cambia el emisor de una tienda y sincroniza los campos fiscales heredados. */
    @Transactional
    public BillingProfileResponse update(UUID storeId, UUID ownerId, BillingProfileRequest request) {
        Store store = storeRepository.findByIdAndOwnerId(storeId, ownerId)
                .orElseThrow(() -> new BusinessException(ValidationError.BILLING_PROFILE_NOT_FOUND));
        String taxId = request.taxId().trim();
        String legalName = request.legalName().trim();
        String address = request.fiscalAddress().trim();
        BillingIssuer issuer = issuerRepository.findByOwnerIdAndTaxId(ownerId, taxId)
                .orElseGet(() -> issuerRepository.save(BillingIssuer.builder()
                        .id(UuidCreator.getTimeOrderedEpoch())
                        .owner(userRepository.getReferenceById(ownerId))
                        .taxId(taxId).legalName(legalName).fiscalAddress(address)
                        .createdAt(OffsetDateTime.now()).updatedAt(OffsetDateTime.now()).build()));
        issuer.update(taxId, legalName, address);
        store.setBillingIssuer(issuer);
        storeRepository.updateLegacyBillingData(issuer.getId(), taxId, legalName);
        return response(issuer);
    }

    /** Devuelve el perfil compartido de una tienda propiedad del usuario. */
    @Transactional(readOnly = true)
    public BillingProfileResponse get(UUID storeId, UUID ownerId) {
        Store store = storeRepository.findByIdAndOwnerId(storeId, ownerId)
                .orElseThrow(() -> new BusinessException(ValidationError.BILLING_PROFILE_NOT_FOUND));
        BillingIssuer issuer = store.getBillingIssuer();
        if (issuer == null) return new BillingProfileResponse(null, store.getTaxId(), store.getLegalName(), "", 1);
        return response(issuer);
    }

    private BillingProfileResponse response(BillingIssuer issuer) {
        return new BillingProfileResponse(issuer.getId(), issuer.getTaxId(), issuer.getLegalName(),
                issuer.getFiscalAddress(), storeRepository.countByBillingIssuerId(issuer.getId()));
    }
}
