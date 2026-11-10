package com.reydiazz.bodeko.core.shopping.cart.exception;

import com.reydiazz.bodeko.shared.exception.BusinessException;

public class ShoppingCartProductNotAvailableException extends BusinessException {

    public ShoppingCartProductNotAvailableException() {
        super(ShoppingCartErrorCode.PRODUCT_NOT_AVAILABLE);
    }

}
