package com.reydiazz.bodeko.core.catalog.web.controller;

import com.reydiazz.bodeko.core.catalog.web.response.PublicStoreResponse;
import com.reydiazz.bodeko.core.catalog.web.response.TopSellingStoreResponse;
import com.reydiazz.bodeko.core.order.core.model.enums.OrderStatus;
import org.springframework.data.domain.PageRequest;
import com.reydiazz.bodeko.core.category.service.CategoryService;
import com.reydiazz.bodeko.core.category.web.response.CategoryResponse;
import com.reydiazz.bodeko.core.store.exception.StoreNotFoundException;
import com.reydiazz.bodeko.core.store.model.entity.Store;
import com.reydiazz.bodeko.core.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

/** Permite consultar las tiendas y sus categorías sin iniciar sesión. */
@RestController
@RequestMapping("/api/public/stores")
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PublicStoreController {
    private final StoreRepository storeRepository;
    private final CategoryService categoryService;

    @GetMapping
    public List<PublicStoreResponse> getAll() {
        return storeRepository.findAll().stream().map(this::toResponse).toList();
    }

    /** Devuelve hasta tres tiendas con más órdenes vendidas en todo su historial. */
    @GetMapping("/top-selling")
    public List<TopSellingStoreResponse> getTopSelling() {
        return storeRepository.findTopSelling(
                List.of(OrderStatus.PAID, OrderStatus.SHIPPED, OrderStatus.DELIVERED), PageRequest.of(0, 3));
    }

    @GetMapping("/by-subdomain/{subdomain}")
    public PublicStoreResponse getBySubdomain(@PathVariable String subdomain) {
        return toResponse(storeRepository.findBySubdomainIgnoreCase(subdomain).orElseThrow(StoreNotFoundException::new));
    }

    @GetMapping("/{storeId}/categories")
    public List<CategoryResponse> getCategories(@PathVariable UUID storeId) {
        if (!storeRepository.existsById(storeId)) throw new StoreNotFoundException();
        return categoryService.getAll(storeId);
    }

    private PublicStoreResponse toResponse(Store store) {
        return new PublicStoreResponse(store.getId(), store.getName(), store.getSubdomain(), store.getCurrency(), store.getTaxRate());
    }
}
