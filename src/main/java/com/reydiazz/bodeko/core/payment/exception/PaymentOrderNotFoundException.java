package com.reydiazz.bodeko.core.payment.exception;

import com.reydiazz.bodeko.shared.exception.BusinessException;

public class PaymentOrderNotFoundException extends BusinessException {

    public PaymentOrderNotFoundException() {
        super(PaymentErrorCode.ORDER_NOT_FOUND);
    }

}
