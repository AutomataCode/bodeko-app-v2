package com.reydiazz.bodeko.core.shopping.cart.exception;

import com.reydiazz.bodeko.shared.exception.BusinessException;

public class ShoppingCartNotFoundException extends BusinessException {

    public ShoppingCartNotFoundException() {
        super(ShoppingCartErrorCode.ENTITY_NOT_FOUND);
    }

}
