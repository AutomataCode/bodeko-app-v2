package com.reydiazz.bodeko.core.order.core.exception;

import com.reydiazz.bodeko.shared.exception.BusinessException;

public class OrderEmptyCartException extends BusinessException {

    public OrderEmptyCartException() {
        super(OrderErrorCode.EMPTY_CART);
    }

}
