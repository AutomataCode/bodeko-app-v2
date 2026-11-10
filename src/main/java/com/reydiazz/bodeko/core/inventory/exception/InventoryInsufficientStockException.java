package com.reydiazz.bodeko.core.inventory.exception;

import com.reydiazz.bodeko.shared.exception.BusinessException;

public class InventoryInsufficientStockException extends BusinessException {

    public InventoryInsufficientStockException(String productName) {
        super("There is not enough stock for the product '%s'.".formatted(productName),
                InventoryErrorCode.INSUFFICIENT_STOCK);
    }

}
