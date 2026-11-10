package com.reydiazz.bodeko.core.catalog.service;

import com.reydiazz.bodeko.core.catalog.web.response.CatalogProductResponse;
import com.reydiazz.bodeko.shared.response.PageResponse;

import java.util.UUID;

public interface CatalogService {

    /** Muestra solo productos activos de la tienda indicada. */
    CatalogProductResponse getById(UUID storeId, UUID productId);

    PageResponse<CatalogProductResponse> search(
            UUID storeId,
            UUID categoryId,
            String search,
            Boolean inStock,
            int page,
            int size
    );

}
