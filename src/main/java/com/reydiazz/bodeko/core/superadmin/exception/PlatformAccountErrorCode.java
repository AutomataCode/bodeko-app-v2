package com.reydiazz.bodeko.core.superadmin.exception;

import com.reydiazz.bodeko.shared.exception.ErrorCode;
import org.springframework.http.HttpStatus;

/** Errores controlados de administración de cuentas. */
public enum PlatformAccountErrorCode implements ErrorCode {
    ACCOUNT_NOT_FOUND("PLATFORM_ACCOUNT_NOT_FOUND", "La cuenta no existe o no corresponde al tipo solicitado.", HttpStatus.NOT_FOUND);
    private final String code;
    private final String message;
    private final HttpStatus httpStatus;
    PlatformAccountErrorCode(String code, String message, HttpStatus httpStatus) { this.code = code; this.message = message; this.httpStatus = httpStatus; }
    public String getCode() { return code; }
    public String getMessage() { return message; }
    public HttpStatus getHttpStatus() { return httpStatus; }
}
