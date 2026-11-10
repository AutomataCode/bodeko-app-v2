package com.reydiazz.bodeko.core.store.exception;

import com.reydiazz.bodeko.shared.exception.BusinessException;

public class StoreSubdomainExistsException extends BusinessException {

    public StoreSubdomainExistsException() {
        super(StoreErrorCode.SUBDOMAIN_ALREADY_IN_USE);
    }

}
