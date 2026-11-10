package com.reydiazz.bodeko.core.shopping.cart.service;

import com.reydiazz.bodeko.core.cart.items.model.entity.ShoppingCartItem;
import com.reydiazz.bodeko.core.shopping.cart.model.entity.ShoppingCart;
import com.reydiazz.bodeko.core.shopping.cart.web.request.AddCartItemRequest;
import com.reydiazz.bodeko.core.shopping.cart.web.request.EditCartItemQuantityRequest;
import com.reydiazz.bodeko.core.shopping.cart.web.response.ShoppingCartResponse;

import java.util.List;
import java.util.UUID;

public interface ShoppingCartService {

    ShoppingCartResponse getCart(UUID userId, UUID storeId);

    ShoppingCartResponse addItem(UUID userId, UUID storeId, AddCartItemRequest request);

    ShoppingCartResponse updateItemQuantity(UUID userId, UUID storeId, UUID productId, EditCartItemQuantityRequest request);

    ShoppingCartResponse removeItem(UUID userId, UUID storeId, UUID productId);

    ShoppingCart findOpenCartForCheckout(UUID userId, UUID storeId);

    List<ShoppingCartItem> findItems(UUID cartId);

    void markAsConverted(ShoppingCart cart);

}
