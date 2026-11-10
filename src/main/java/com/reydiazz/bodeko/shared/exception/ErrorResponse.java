package com.reydiazz.bodeko.shared.exception;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ErrorResponse {

    private final int status;
    private final String code;
    private final String message;
    private final List<FieldErrorResponse> fieldErrors;
    private final LocalDateTime timestamp;

    @Builder
    private ErrorResponse(int status, String code, String message, List<FieldErrorResponse> fieldErrors) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.fieldErrors = fieldErrors;
        this.timestamp = LocalDateTime.now();
    }

}