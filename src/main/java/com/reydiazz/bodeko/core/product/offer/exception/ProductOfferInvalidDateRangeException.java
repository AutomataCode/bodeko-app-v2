package com.reydiazz.bodeko.core.product.offer.exception;

import com.reydiazz.bodeko.shared.exception.BusinessException;

public class ProductOfferInvalidDateRangeException extends BusinessException {

    public ProductOfferInvalidDateRangeException() {
        super(ProductOfferErrorCode.INVALID_DATE_RANGE);
    }

}
