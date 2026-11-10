package com.reydiazz.bodeko.core.payment.exception;

import com.reydiazz.bodeko.shared.exception.BusinessException;

public class PaymentRefundRejectedException extends BusinessException {

    public PaymentRefundRejectedException(String reason) {
        super(reason != null && !reason.isBlank() ? reason : PaymentErrorCode.REFUND_REJECTED.getMessage(),
                PaymentErrorCode.REFUND_REJECTED);
    }

}
