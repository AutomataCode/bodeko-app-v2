package com.reydiazz.bodeko.shared.exception;

import org.springframework.http.HttpStatus;

public interface ErrorCode {

    HttpStatus getHttpStatus();

    String getMessage();

    String getCode();

}
