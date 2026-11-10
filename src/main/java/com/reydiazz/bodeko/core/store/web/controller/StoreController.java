package com.reydiazz.bodeko.core.store.web.controller;

import com.reydiazz.bodeko.core.store.service.StoreService;
import com.reydiazz.bodeko.core.store.util.StoreRouters;
import com.reydiazz.bodeko.core.store.web.request.CreateStoreRequest;
import com.reydiazz.bodeko.core.store.web.request.EditTaxRateRequest;
import com.reydiazz.bodeko.core.store.web.response.StoreResponse;
import com.reydiazz.bodeko.security.config.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(StoreRouters.API_BASE)
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    @PostMapping
    public ResponseEntity<StoreResponse> create(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody CreateStoreRequest request) {
        StoreResponse response = storeService.create(principal.user().getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<StoreResponse>> getAll(@AuthenticationPrincipal UserPrincipal principal) {
        List<StoreResponse> response = storeService.getAllByOwner(principal.user().getId());
        return ResponseEntity.ok(response);
    }

    @GetMapping(StoreRouters.STORE_ID)
    public ResponseEntity<StoreResponse> getById(@PathVariable UUID storeId) {
        StoreResponse response = storeService.getById(storeId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping(StoreRouters.TAX_RATE)
    public ResponseEntity<StoreResponse> updateTaxRate(
            @PathVariable UUID storeId,
            @Valid @RequestBody EditTaxRateRequest request) {
        StoreResponse response = storeService.updateTaxRate(storeId, request);
        return ResponseEntity.ok(response);
    }

}
