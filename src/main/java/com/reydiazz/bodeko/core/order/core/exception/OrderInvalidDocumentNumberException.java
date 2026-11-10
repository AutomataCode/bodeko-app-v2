package com.reydiazz.bodeko.core.order.core.exception;

import com.reydiazz.bodeko.shared.exception.BusinessException;

public class OrderInvalidDocumentNumberException extends BusinessException {

    public OrderInvalidDocumentNumberException() {
        super(OrderErrorCode.INVALID_DOCUMENT_NUMBER);
    }

}
