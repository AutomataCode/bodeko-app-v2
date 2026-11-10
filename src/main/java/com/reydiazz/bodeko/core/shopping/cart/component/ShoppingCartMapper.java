package com.reydiazz.bodeko.core.shopping.cart.component;

import com.reydiazz.bodeko.core.cart.items.model.entity.ShoppingCartItem;
import com.reydiazz.bodeko.core.shopping.cart.model.entity.ShoppingCart;
import com.reydiazz.bodeko.core.shopping.cart.web.response.ShoppingCartItemResponse;
import com.reydiazz.bodeko.core.shopping.cart.web.response.ShoppingCartResponse;
import com.reydiazz.bodeko.core.product.core.model.entity.Product;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class ShoppingCartMapper {

    public ShoppingCartResponse toResponse(
            ShoppingCart cart,
            UUID storeId,
            List<ShoppingCartItem> items,
            Map<UUID, BigDecimal> offerPrices
    ) {
        List<ShoppingCartItemResponse> itemResponses = items.stream()
                .map(item -> toItemResponse(item, offerPrices))
                .toList();
        int totalQuantity = itemResponses.stream()
                .mapToInt(ShoppingCartItemResponse::quantity)
                .sum();
        BigDecimal subtotal = itemResponses.stream()
                .map(ShoppingCartItemResponse::itemSubtotal)
                .reduce(BigDecimal.ZERO.setScale(2), BigDecimal::add);
        return new ShoppingCartResponse(
                cart != null ? cart.getId() : null,
                storeId,
                itemResponses,
                totalQuantity,
                subtotal
        );
    }

    private ShoppingCartItemResponse toItemResponse(ShoppingCartItem item, Map<UUID, BigDecimal> offerPrices) {
        Product product = item.getProduct();
        BigDecimal chargedUnitPrice = offerPrices.getOrDefault(product.getId(), product.getBasePrice());
        BigDecimal itemSubtotal = chargedUnitPrice.multiply(BigDecimal.valueOf(item.getQuantity()));
        return new ShoppingCartItemResponse(
                product.getId(),
                product.getName(),
                product.getImageUrl(),
                item.getQuantity(),
                product.getBasePrice(),
                chargedUnitPrice,
                itemSubtotal
        );
    }

}
