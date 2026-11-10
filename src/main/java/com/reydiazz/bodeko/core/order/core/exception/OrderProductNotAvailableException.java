package com.reydiazz.bodeko.core.order.core.exception;

import com.reydiazz.bodeko.shared.exception.BusinessException;

public class OrderProductNotAvailableException extends BusinessException {

    public OrderProductNotAvailableException(String productName) {
        super("The product '%s' is no longer available for purchase.".formatted(productName),
                OrderErrorCode.PRODUCT_NOT_AVAILABLE);
    }

}
