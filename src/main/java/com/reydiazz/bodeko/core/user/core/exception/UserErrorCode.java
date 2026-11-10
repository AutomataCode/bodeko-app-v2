package com.reydiazz.bodeko.core.user.core.exception;

import com.reydiazz.bodeko.shared.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {

    EMAIL_ALREADY_IN_USE(
            "USER_EMAIL_EXIST",
            "The email is already in use.",
            HttpStatus.CONFLICT
    );

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

}
