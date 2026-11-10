package com.reydiazz.bodeko.shared.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private final ErrorCode error;

    public BusinessException(ErrorCode error) {
        super(error.getMessage());
        this.error = error;
    }

    public BusinessException(String message, ErrorCode error) {
        super(message);
        this.error = error;
    }

}