package com.reydiazz.bodeko.core.order.management.exception;

import com.reydiazz.bodeko.core.order.core.model.enums.OrderStatus;
import com.reydiazz.bodeko.shared.exception.BusinessException;

public class OrderManagementInvalidTransitionException extends BusinessException {

    public OrderManagementInvalidTransitionException(OrderStatus from, OrderStatus to) {
        super("An order in status " + from + " cannot move to " + to + ".",
                OrderManagementErrorCode.INVALID_STATUS_TRANSITION);
    }

}
