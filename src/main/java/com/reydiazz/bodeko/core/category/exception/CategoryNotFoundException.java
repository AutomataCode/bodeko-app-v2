package com.reydiazz.bodeko.core.category.exception;

import com.reydiazz.bodeko.shared.exception.BusinessException;

public class CategoryNotFoundException extends BusinessException {

    public CategoryNotFoundException() {
        super(CategoryErrorCode.ENTITY_NOT_FOUND);
    }

}
