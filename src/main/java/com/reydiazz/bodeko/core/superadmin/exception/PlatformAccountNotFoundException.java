package com.reydiazz.bodeko.core.superadmin.exception;

import com.reydiazz.bodeko.shared.exception.BusinessException;

/** La cuenta solicitada no pertenece al grupo administrable. */
public class PlatformAccountNotFoundException extends BusinessException {
    public PlatformAccountNotFoundException() { super(PlatformAccountErrorCode.ACCOUNT_NOT_FOUND); }
}
