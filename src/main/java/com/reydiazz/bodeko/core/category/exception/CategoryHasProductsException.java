package com.reydiazz.bodeko.core.category.exception;

import com.reydiazz.bodeko.shared.exception.BusinessException;

public class CategoryHasProductsException extends BusinessException {

    public CategoryHasProductsException() {
        super(CategoryErrorCode.HAS_PRODUCTS);
    }

}
