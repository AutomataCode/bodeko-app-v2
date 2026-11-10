package com.reydiazz.bodeko.core.order.core.exception;

import com.reydiazz.bodeko.shared.exception.BusinessException;

public class OrderReceiptRequiresDniException extends BusinessException {

    public OrderReceiptRequiresDniException() {
        super(OrderErrorCode.RECEIPT_REQUIRES_DNI);
    }

}
