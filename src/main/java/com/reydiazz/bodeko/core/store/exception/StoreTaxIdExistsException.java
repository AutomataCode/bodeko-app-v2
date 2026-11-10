package com.reydiazz.bodeko.core.store.exception;

import com.reydiazz.bodeko.shared.exception.BusinessException;

public class StoreTaxIdExistsException extends BusinessException {

    public StoreTaxIdExistsException() {
        super(StoreErrorCode.TAX_ID_ALREADY_IN_USE);
    }

}
