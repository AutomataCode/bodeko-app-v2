package com.reydiazz.bodeko.core.order.core.exception;

import com.reydiazz.bodeko.shared.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum OrderErrorCode implements ErrorCode {

    EMPTY_CART(
            "ORDER_EMPTY_CART",
            "The shopping cart is empty.",
            HttpStatus.BAD_REQUEST
    ),

    INVOICE_REQUIRES_RUC(
            "ORDER_INVOICE_REQUIRES_RUC",
            "An invoice requires a tax ID (RUC) as the customer document.",
            HttpStatus.BAD_REQUEST
    ),

    INVOICE_REQUIRES_BUSINESS_NAME(
            "ORDER_INVOICE_REQUIRES_BUSINESS_NAME",
            "The business name is required to issue an invoice.",
            HttpStatus.BAD_REQUEST
    ),

    RECEIPT_REQUIRES_DNI(
            "ORDER_RECEIPT_REQUIRES_DNI",
            "A receipt requires a DNI as the customer document.",
            HttpStatus.BAD_REQUEST
    ),

    INVALID_DOCUMENT_NUMBER(
            "ORDER_INVALID_DOCUMENT_NUMBER",
            "The document number does not match the document type.",
            HttpStatus.BAD_REQUEST
    ),

    PRODUCT_NOT_AVAILABLE(
            "ORDER_PRODUCT_NOT_AVAILABLE",
            "A product in the shopping cart is not available for purchase.",
            HttpStatus.CONFLICT
    );

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

}
