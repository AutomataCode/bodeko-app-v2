package com.reydiazz.bodeko.core.product.offer.web.controller;

import com.reydiazz.bodeko.core.product.offer.service.ProductOfferService;
import com.reydiazz.bodeko.core.product.offer.util.ProductOfferRouters;
import com.reydiazz.bodeko.core.product.offer.web.request.CreateProductOfferRequest;
import com.reydiazz.bodeko.core.product.offer.web.response.ProductOfferResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(ProductOfferRouters.API_BASE)
@RequiredArgsConstructor
public class ProductOfferController {

    private final ProductOfferService productOfferService;

    @PostMapping
    public ResponseEntity<ProductOfferResponse> create(
            @PathVariable UUID storeId,
            @PathVariable UUID productId,
            @Valid @RequestBody CreateProductOfferRequest request) {
        ProductOfferResponse response = productOfferService.create(productId, storeId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ProductOfferResponse>> getAll(
            @PathVariable UUID storeId,
            @PathVariable UUID productId) {
        List<ProductOfferResponse> response = productOfferService.getAll(productId, storeId);
        return ResponseEntity.ok(response);
    }

}
