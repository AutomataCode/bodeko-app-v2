package com.reydiazz.bodeko.core.order.management.exception;

import com.reydiazz.bodeko.shared.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum OrderManagementErrorCode implements ErrorCode {

    ENTITY_NOT_FOUND(
            "ORDER_NOT_FOUND",
            "The order does not exist.",
            HttpStatus.NOT_FOUND
    ),

    INVALID_STATUS_TRANSITION(
            "ORDER_INVALID_STATUS_TRANSITION",
            "The order cannot move to the requested status.",
            HttpStatus.CONFLICT
    );

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

}
