package com.reydiazz.bodeko.core.product.offer.exception;

import com.reydiazz.bodeko.shared.exception.BusinessException;

public class ProductOfferOverlapException extends BusinessException {

    public ProductOfferOverlapException() {
        super(ProductOfferErrorCode.DATE_RANGE_OVERLAP);
    }

}
