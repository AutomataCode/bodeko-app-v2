package com.reydiazz.bodeko.core.payment.exception;

import com.reydiazz.bodeko.shared.exception.BusinessException;

public class PaymentInvalidAmountException extends BusinessException {

    public PaymentInvalidAmountException() {
        super(PaymentErrorCode.INVALID_AMOUNT);
    }

}
