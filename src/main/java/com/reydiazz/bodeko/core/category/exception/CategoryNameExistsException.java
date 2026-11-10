package com.reydiazz.bodeko.core.category.exception;

import com.reydiazz.bodeko.shared.exception.BusinessException;

public class CategoryNameExistsException extends BusinessException {

    public CategoryNameExistsException() {
        super(CategoryErrorCode.NAME_ALREADY_IN_USE);
    }

}
