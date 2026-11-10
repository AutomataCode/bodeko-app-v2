package com.reydiazz.bodeko.core.shopping.cart.web.response;

import java.math.BigDecimal;
import java.util.UUID;

public record ShoppingCartItemResponse(
        UUID productId,
        String name,
        String imageUrl,
        int quantity,
        BigDecimal listUnitPrice,
        BigDecimal chargedUnitPrice,
        BigDecimal itemSubtotal
) {
}
