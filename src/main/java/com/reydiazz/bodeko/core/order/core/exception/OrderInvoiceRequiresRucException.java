package com.reydiazz.bodeko.core.order.core.exception;

import com.reydiazz.bodeko.shared.exception.BusinessException;

public class OrderInvoiceRequiresRucException extends BusinessException {

    public OrderInvoiceRequiresRucException() {
        super(OrderErrorCode.INVOICE_REQUIRES_RUC);
    }

}
