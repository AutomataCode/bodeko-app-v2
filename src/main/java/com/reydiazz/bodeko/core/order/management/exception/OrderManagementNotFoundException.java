package com.reydiazz.bodeko.core.order.management.exception;

import com.reydiazz.bodeko.shared.exception.BusinessException;

public class OrderManagementNotFoundException extends BusinessException {

    public OrderManagementNotFoundException() {
        super(OrderManagementErrorCode.ENTITY_NOT_FOUND);
    }

}
