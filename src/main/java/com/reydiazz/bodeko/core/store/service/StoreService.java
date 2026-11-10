package com.reydiazz.bodeko.core.store.service;

import com.reydiazz.bodeko.core.store.model.entity.Store;
import com.reydiazz.bodeko.core.store.web.request.CreateStoreRequest;
import com.reydiazz.bodeko.core.store.web.request.EditTaxRateRequest;
import com.reydiazz.bodeko.core.store.web.response.StoreResponse;

import java.util.List;
import java.util.UUID;

public interface StoreService {

    StoreResponse create(UUID ownerId, CreateStoreRequest request);

    List<StoreResponse> getAllByOwner(UUID ownerId);

    StoreResponse getById(UUID id);

    StoreResponse updateTaxRate(UUID id, EditTaxRateRequest request);

    Store findEntityById(UUID id);

    Store getReferenceById(UUID id);

}
