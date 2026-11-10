package com.reydiazz.bodeko.core.payment.exception;

import com.reydiazz.bodeko.shared.exception.BusinessException;

public class PaymentDeclinedException extends BusinessException {

    public PaymentDeclinedException(String reason) {
        super(reason != null && !reason.isBlank() ? reason : PaymentErrorCode.DECLINED.getMessage(),
                PaymentErrorCode.DECLINED);
    }

}
