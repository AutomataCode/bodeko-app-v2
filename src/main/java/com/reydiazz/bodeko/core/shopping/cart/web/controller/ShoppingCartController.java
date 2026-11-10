package com.reydiazz.bodeko.core.shopping.cart.web.controller;

import com.reydiazz.bodeko.core.shopping.cart.service.ShoppingCartService;
import com.reydiazz.bodeko.core.shopping.cart.util.ShoppingCartRouters;
import com.reydiazz.bodeko.core.shopping.cart.web.request.AddCartItemRequest;
import com.reydiazz.bodeko.core.shopping.cart.web.request.EditCartItemQuantityRequest;
import com.reydiazz.bodeko.core.shopping.cart.web.response.ShoppingCartResponse;
import com.reydiazz.bodeko.security.config.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(ShoppingCartRouters.API_BASE)
@RequiredArgsConstructor
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;

    @GetMapping
    public ResponseEntity<ShoppingCartResponse> getCart(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable UUID storeId) {
        ShoppingCartResponse response = shoppingCartService.getCart(principal.user().getId(), storeId);
        return ResponseEntity.ok(response);
    }

    @PostMapping(ShoppingCartRouters.ITEMS)
    public ResponseEntity<ShoppingCartResponse> addItem(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable UUID storeId,
            @Valid @RequestBody AddCartItemRequest request) {
        ShoppingCartResponse response = shoppingCartService.addItem(principal.user().getId(), storeId, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping(ShoppingCartRouters.ITEM_ID)
    public ResponseEntity<ShoppingCartResponse> updateItemQuantity(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable UUID storeId,
            @PathVariable UUID productId,
            @Valid @RequestBody EditCartItemQuantityRequest request) {
        ShoppingCartResponse response =
                shoppingCartService.updateItemQuantity(principal.user().getId(), storeId, productId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping(ShoppingCartRouters.ITEM_ID)
    public ResponseEntity<ShoppingCartResponse> removeItem(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable UUID storeId,
            @PathVariable UUID productId) {
        ShoppingCartResponse response =
                shoppingCartService.removeItem(principal.user().getId(), storeId, productId);
        return ResponseEntity.ok(response);
    }

}
