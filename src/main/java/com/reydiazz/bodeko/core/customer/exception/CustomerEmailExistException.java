package com.reydiazz.bodeko.core.customer.exception;

import com.reydiazz.bodeko.core.user.core.exception.UserErrorCode;
import com.reydiazz.bodeko.shared.exception.BusinessException;

/** El correo ya está registrado en esta tienda. */
public class CustomerEmailExistException extends BusinessException {

    public CustomerEmailExistException() {
        super(UserErrorCode.EMAIL_ALREADY_IN_USE);
    }
}
