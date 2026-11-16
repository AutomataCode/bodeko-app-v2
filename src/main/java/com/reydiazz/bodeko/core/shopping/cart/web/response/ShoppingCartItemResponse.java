package com.reydiazz.bodeko.core.shopping.cart.web.response;

import java.math.BigDecimal;
import java.util.UUID;

public record ShoppingCartItemResponse(
        UUID productId,
        UUID variantId,
        String sku,
        java.util.Map<String, String> options,
        String name,
        String imageUrl,
        int quantity,
        BigDecimal listUnitPrice,
        BigDecimal chargedUnitPrice,
        BigDecimal itemSubtotal
) {
}
