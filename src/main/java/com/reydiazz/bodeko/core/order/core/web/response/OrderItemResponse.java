package com.reydiazz.bodeko.core.order.core.web.response;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderItemResponse(
        UUID productId,
        String productName,
        UUID variantId,
        String variantSku,
        java.util.Map<String, String> variantOptions,
        int quantity,
        BigDecimal listUnitPrice,
        BigDecimal chargedUnitPrice,
        BigDecimal itemSubtotal
) {
}
