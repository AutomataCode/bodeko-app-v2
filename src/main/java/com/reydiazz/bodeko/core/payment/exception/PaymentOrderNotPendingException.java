package com.reydiazz.bodeko.core.payment.exception;

import com.reydiazz.bodeko.shared.exception.BusinessException;

public class PaymentOrderNotPendingException extends BusinessException {

    public PaymentOrderNotPendingException() {
        super(PaymentErrorCode.ORDER_NOT_PENDING);
    }

}
