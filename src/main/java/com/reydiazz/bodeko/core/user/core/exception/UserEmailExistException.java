package com.reydiazz.bodeko.core.user.core.exception;

import com.reydiazz.bodeko.shared.exception.BusinessException;

public class UserEmailExistException extends BusinessException {

    public UserEmailExistException() {
        super(UserErrorCode.EMAIL_ALREADY_IN_USE);
    }
}
