package com.reydiazz.bodeko.core.store.exception;

import com.reydiazz.bodeko.shared.exception.BusinessException;

public class StoreNotFoundException extends BusinessException {

    public StoreNotFoundException() {
        super(StoreErrorCode.ENTITY_NOT_FOUND);
    }

}
