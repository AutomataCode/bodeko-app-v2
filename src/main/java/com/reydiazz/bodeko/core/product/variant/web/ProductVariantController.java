package com.reydiazz.bodeko.core.product.variant.web;

import com.reydiazz.bodeko.core.product.variant.service.ProductVariantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/stores/{storeId}/products/{productId}/variants")
@RequiredArgsConstructor
public class ProductVariantController {
    private final ProductVariantService service;

    @GetMapping
    public List<ProductVariantResponse> list(@PathVariable UUID storeId,@PathVariable UUID productId) {
        return service.list(productId,storeId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductVariantResponse create(@PathVariable UUID storeId,@PathVariable UUID productId,
                                         @Valid @RequestBody CreateProductVariantRequest request) {
        return service.create(productId,storeId,request);
    }

    @PutMapping("/{variantId}")
    public ProductVariantResponse update(@PathVariable UUID storeId, @PathVariable UUID productId,
                                         @PathVariable UUID variantId,
                                         @Valid @RequestBody CreateProductVariantRequest request) {
        return service.update(productId, variantId, storeId, request);
    }

    @PatchMapping("/{variantId}/status")
    public ProductVariantResponse changeStatus(@PathVariable UUID storeId, @PathVariable UUID productId,
                                               @PathVariable UUID variantId,
                                               @Valid @RequestBody ChangeProductVariantStatusRequest request) {
        return service.changeStatus(productId, variantId, storeId, request.active());
    }
}
