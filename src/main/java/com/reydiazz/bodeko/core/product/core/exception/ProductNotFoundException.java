package com.reydiazz.bodeko.core.product.core.exception;

import com.reydiazz.bodeko.shared.exception.BusinessException;

public class ProductNotFoundException extends BusinessException {

    public ProductNotFoundException() {
        super(ProductErrorCode.ENTITY_NOT_FOUND);
    }

}
