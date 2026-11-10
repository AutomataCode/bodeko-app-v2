package com.reydiazz.bodeko.core.store.service;

import com.github.f4b6a3.uuid.UuidCreator;
import com.reydiazz.bodeko.core.store.component.StoreMapper;
import com.reydiazz.bodeko.core.store.exception.StoreNotFoundException;
import com.reydiazz.bodeko.core.store.exception.StoreSubdomainExistsException;
import com.reydiazz.bodeko.core.store.exception.StoreTaxIdExistsException;
import com.reydiazz.bodeko.core.store.model.entity.Store;
import com.reydiazz.bodeko.core.store.repository.StoreRepository;
import com.reydiazz.bodeko.core.store.web.request.CreateStoreRequest;
import com.reydiazz.bodeko.core.store.web.request.EditTaxRateRequest;
import com.reydiazz.bodeko.core.store.web.response.StoreResponse;
import com.reydiazz.bodeko.core.user.core.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;
    private final StoreMapper storeMapper;

    private final UserService userService;

    @Override
    @Transactional
    public StoreResponse create(UUID ownerId, CreateStoreRequest request) {
        String taxId = request.taxId().trim();
        String subdomain = request.subdomain().trim().toLowerCase();
        verifyTaxId(taxId);
        verifySubdomain(subdomain);
        Store store = Store.builder()
                .id(UuidCreator.getTimeOrderedEpoch())
                .owner(userService.getReferenceById(ownerId))
                .name(request.name().trim())
                .taxId(taxId)
                .legalName(request.legalName().trim())
                .subdomain(subdomain)
                .createdAt(OffsetDateTime.now())
                .build();
        Store storeSaved = storeRepository.save(store);
        return storeMapper.toResponse(storeSaved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StoreResponse> getAllByOwner(UUID ownerId) {
        return storeRepository.findAllByOwnerId(ownerId).stream()
                .map(storeMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public StoreResponse getById(UUID id) {
        Store store = findEntityById(id);
        return storeMapper.toResponse(store);
    }

    @Override
    @Transactional
    public StoreResponse updateTaxRate(UUID id, EditTaxRateRequest request) {
        Store store = findEntityById(id);
        store.updateTaxRate(request.taxRate());
        return storeMapper.toResponse(store);
    }

    @Override
    @Transactional(readOnly = true)
    public Store findEntityById(UUID id) {
        return storeRepository.findById(id)
                .orElseThrow(StoreNotFoundException::new);
    }

    @Override
    public Store getReferenceById(UUID id) {
        return storeRepository.getReferenceById(id);
    }

    private void verifyTaxId(String taxId) {
        if (storeRepository.existsByTaxId(taxId)) {
            throw new StoreTaxIdExistsException();
        }
    }

    private void verifySubdomain(String subdomain) {
        if (storeRepository.existsBySubdomain(subdomain)) {
            throw new StoreSubdomainExistsException();
        }
    }

}
