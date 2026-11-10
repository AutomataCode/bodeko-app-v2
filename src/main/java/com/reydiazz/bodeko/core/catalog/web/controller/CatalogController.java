package com.reydiazz.bodeko.core.catalog.web.controller;

import com.reydiazz.bodeko.core.catalog.service.CatalogService;
import com.reydiazz.bodeko.core.catalog.util.CatalogRouters;
import com.reydiazz.bodeko.core.catalog.web.response.CatalogProductResponse;
import com.reydiazz.bodeko.shared.response.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping(CatalogRouters.API_BASE)
@RequiredArgsConstructor
public class CatalogController {

    private final CatalogService catalogService;

    @GetMapping("/{productId}")
    public ResponseEntity<CatalogProductResponse> getById(@PathVariable UUID storeId, @PathVariable UUID productId) {
        return ResponseEntity.ok(catalogService.getById(storeId, productId));
    }

    @GetMapping
    public ResponseEntity<PageResponse<CatalogProductResponse>> search(
            @PathVariable UUID storeId,
            @RequestParam(required = false) UUID categoryId,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Boolean inStock,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        PageResponse<CatalogProductResponse> response =
                catalogService.search(storeId, categoryId, search, inStock, page, size);
        return ResponseEntity.ok(response);
    }

}
