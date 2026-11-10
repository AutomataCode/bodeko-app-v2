package com.reydiazz.bodeko.shared.exception;

public record FieldErrorResponse(
        String field,
        String message
) {
}