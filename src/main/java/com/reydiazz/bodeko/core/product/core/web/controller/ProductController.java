package com.reydiazz.bodeko.core.product.core.web.controller;

import com.reydiazz.bodeko.core.product.core.service.ProductService;
import com.reydiazz.bodeko.core.product.core.util.ProductRouters;
import com.reydiazz.bodeko.core.product.core.web.request.ChangeProductStatusRequest;
import com.reydiazz.bodeko.core.product.core.web.request.CreateProductRequest;
import com.reydiazz.bodeko.core.product.core.web.request.EditProductRequest;
import com.reydiazz.bodeko.core.product.core.web.response.ProductResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(ProductRouters.API_BASE)
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponse> create(
            @PathVariable UUID storeId,
            @Valid @RequestBody CreateProductRequest request) {
        ProductResponse response = productService.create(storeId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAll(@PathVariable UUID storeId) {
        List<ProductResponse> response = productService.getAll(storeId);
        return ResponseEntity.ok(response);
    }

    @GetMapping(ProductRouters.PRODUCT_ID)
    public ResponseEntity<ProductResponse> getById(
            @PathVariable UUID storeId,
            @PathVariable UUID productId) {
        ProductResponse response = productService.getById(productId, storeId);
        return ResponseEntity.ok(response);
    }

    @PutMapping(ProductRouters.PRODUCT_ID)
    public ResponseEntity<ProductResponse> update(
            @PathVariable UUID storeId,
            @PathVariable UUID productId,
            @Valid @RequestBody EditProductRequest request) {
        ProductResponse response = productService.update(productId, storeId, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping(ProductRouters.STATUS)
    public ResponseEntity<ProductResponse> changeStatus(
            @PathVariable UUID storeId,
            @PathVariable UUID productId,
            @Valid @RequestBody ChangeProductStatusRequest request) {
        ProductResponse response = productService.changeStatus(productId, storeId, request);
        return ResponseEntity.ok(response);
    }

}
