package com.reydiazz.bodeko.core.order.core.web.response;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderItemResponse(
        UUID productId,
        String productName,
        int quantity,
        BigDecimal listUnitPrice,
        BigDecimal chargedUnitPrice,
        BigDecimal itemSubtotal
) {
}
