package com.reydiazz.bodeko.core.shopping.cart.exception;

import com.reydiazz.bodeko.shared.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ShoppingCartErrorCode implements ErrorCode {

    ENTITY_NOT_FOUND(
            "CART_NOT_FOUND",
            "The shopping cart does not exist.",
            HttpStatus.NOT_FOUND
    ),

    ITEM_NOT_FOUND(
            "CART_ITEM_NOT_FOUND",
            "The product is not in the shopping cart.",
            HttpStatus.NOT_FOUND
    ),

    PRODUCT_NOT_AVAILABLE(
            "CART_PRODUCT_NOT_AVAILABLE",
            "The product is not available for purchase.",
            HttpStatus.CONFLICT
    );

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

}
