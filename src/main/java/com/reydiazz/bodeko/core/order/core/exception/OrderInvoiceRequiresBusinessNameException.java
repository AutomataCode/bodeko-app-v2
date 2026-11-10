package com.reydiazz.bodeko.core.order.core.exception;

import com.reydiazz.bodeko.shared.exception.BusinessException;

public class OrderInvoiceRequiresBusinessNameException extends BusinessException {

    public OrderInvoiceRequiresBusinessNameException() {
        super(OrderErrorCode.INVOICE_REQUIRES_BUSINESS_NAME);
    }

}
