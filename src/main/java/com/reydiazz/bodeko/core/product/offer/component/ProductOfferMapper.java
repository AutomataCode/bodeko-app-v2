package com.reydiazz.bodeko.core.product.offer.component;

import com.reydiazz.bodeko.core.product.offer.model.entity.ProductOffer;
import com.reydiazz.bodeko.core.product.offer.web.response.ProductOfferResponse;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;

@Component
public class ProductOfferMapper {

    public ProductOfferResponse toResponse(ProductOffer offer) {
        return new ProductOfferResponse(
                offer.getProductId(),
                offer.getOfferPrice(),
                offer.getStartsAt(),
                offer.getEndsAt(),
                offer.isActiveAt(OffsetDateTime.now()),
                offer.getCreatedAt()
        );
    }

}
