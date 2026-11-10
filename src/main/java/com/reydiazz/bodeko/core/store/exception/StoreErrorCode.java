package com.reydiazz.bodeko.core.store.exception;

import com.reydiazz.bodeko.shared.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum StoreErrorCode implements ErrorCode {

    TAX_ID_ALREADY_IN_USE(
            "STORE_TAX_ID_ALREADY_IN_USE",
            "The tax ID (RUC) is already in use.",
            HttpStatus.CONFLICT
    ),

    SUBDOMAIN_ALREADY_IN_USE(
            "STORE_SUBDOMAIN_ALREADY_IN_USE",
            "The subdomain is already in use.",
            HttpStatus.CONFLICT
    ),

    ENTITY_NOT_FOUND(
            "STORE_NOT_FOUND",
            "The store does not exist.",
            HttpStatus.NOT_FOUND
    );

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

}
