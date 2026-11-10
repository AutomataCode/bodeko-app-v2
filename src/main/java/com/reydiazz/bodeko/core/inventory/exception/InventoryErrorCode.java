package com.reydiazz.bodeko.core.inventory.exception;

import com.reydiazz.bodeko.shared.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum InventoryErrorCode implements ErrorCode {

    ENTITY_NOT_FOUND(
            "INVENTORY_NOT_FOUND",
            "The inventory does not exist.",
            HttpStatus.NOT_FOUND
    ),

    INSUFFICIENT_STOCK(
            "INVENTORY_INSUFFICIENT_STOCK",
            "There is not enough stock for the requested quantity.",
            HttpStatus.CONFLICT
    );

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

}
