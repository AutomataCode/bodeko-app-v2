package com.reydiazz.bodeko.core.shopping.cart.exception;

import com.reydiazz.bodeko.shared.exception.BusinessException;

public class ShoppingCartItemNotFoundException extends BusinessException {

    public ShoppingCartItemNotFoundException() {
        super(ShoppingCartErrorCode.ITEM_NOT_FOUND);
    }

}
