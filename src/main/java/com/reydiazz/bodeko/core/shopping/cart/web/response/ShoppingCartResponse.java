package com.reydiazz.bodeko.core.shopping.cart.web.response;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record ShoppingCartResponse(
        UUID id,
        UUID storeId,
        List<ShoppingCartItemResponse> items,
        int totalQuantity,
        BigDecimal subtotal
) {
}
