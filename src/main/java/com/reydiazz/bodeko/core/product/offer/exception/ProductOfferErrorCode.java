package com.reydiazz.bodeko.core.product.offer.exception;

import com.reydiazz.bodeko.shared.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ProductOfferErrorCode implements ErrorCode {

    INVALID_DATE_RANGE(
            "OFFER_INVALID_DATE_RANGE",
            "The end date must be after the start date.",
            HttpStatus.BAD_REQUEST
    ),

    DATE_RANGE_OVERLAP(
            "OFFER_DATE_RANGE_OVERLAP",
            "The product already has an offer in the given date range.",
            HttpStatus.CONFLICT
    );

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

}
