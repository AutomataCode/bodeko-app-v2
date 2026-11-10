package com.reydiazz.bodeko.core.payment.exception;

import com.reydiazz.bodeko.shared.exception.BusinessException;

public class PaymentGatewayException extends BusinessException {

    public PaymentGatewayException(PaymentErrorCode error) {
        super(error);
    }

    public PaymentGatewayException(String message, PaymentErrorCode error) {
        super(message != null && !message.isBlank() ? message : error.getMessage(), error);
    }

}
