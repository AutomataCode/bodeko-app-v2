package com.reydiazz.bodeko.core.product.core.exception;

import com.reydiazz.bodeko.shared.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ProductErrorCode implements ErrorCode {

    ENTITY_NOT_FOUND(
            "PRODUCT_NOT_FOUND",
            "The product does not exist.",
            HttpStatus.NOT_FOUND
    );

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

}
