package com.reydiazz.bodeko.core.payment.exception;

import com.reydiazz.bodeko.shared.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PaymentErrorCode implements ErrorCode {

    ORDER_NOT_FOUND(
            "PAYMENT_ORDER_NOT_FOUND",
            "The order does not exist.",
            HttpStatus.NOT_FOUND
    ),

    ORDER_NOT_PENDING(
            "PAYMENT_ORDER_NOT_PENDING",
            "Only orders in PENDING status can be paid.",
            HttpStatus.CONFLICT
    ),

    INVALID_AMOUNT(
            "PAYMENT_INVALID_AMOUNT",
            "The payment amount must be greater than zero.",
            HttpStatus.BAD_REQUEST
    ),

    DECLINED(
            "PAYMENT_DECLINED",
            "The payment was declined by the payment gateway.",
            HttpStatus.PAYMENT_REQUIRED
    ),

    REFUND_REJECTED(
            "PAYMENT_REFUND_REJECTED",
            "The payment gateway could not refund the payment.",
            HttpStatus.UNPROCESSABLE_ENTITY
    ),

    GATEWAY_INVALID_REQUEST(
            "PAYMENT_GATEWAY_INVALID_REQUEST",
            "The payment gateway rejected the request.",
            HttpStatus.BAD_REQUEST
    ),

    GATEWAY_UNAVAILABLE(
            "PAYMENT_GATEWAY_UNAVAILABLE",
            "The payment gateway is not available. Please try again later.",
            HttpStatus.BAD_GATEWAY
    );

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

}
