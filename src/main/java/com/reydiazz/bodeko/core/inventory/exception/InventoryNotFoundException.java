package com.reydiazz.bodeko.core.inventory.exception;

import com.reydiazz.bodeko.shared.exception.BusinessException;

public class InventoryNotFoundException extends BusinessException {

    public InventoryNotFoundException() {
        super(InventoryErrorCode.ENTITY_NOT_FOUND);
    }

}
