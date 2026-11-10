package com.reydiazz.bodeko.core.product.offer.service;

import com.reydiazz.bodeko.core.product.core.model.entity.Product;
import com.reydiazz.bodeko.core.product.offer.web.request.CreateProductOfferRequest;
import com.reydiazz.bodeko.core.product.offer.web.response.ProductOfferResponse;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ProductOfferService {

    List<ProductOfferResponse> getAll(UUID productId, UUID storeId);

    ProductOfferResponse create(UUID productId, UUID storeId, CreateProductOfferRequest request);

    BigDecimal resolveEffectivePrice(Product product, OffsetDateTime moment);

    Map<UUID, BigDecimal> findActiveOfferPrices(Collection<UUID> productIds, OffsetDateTime moment);

}
