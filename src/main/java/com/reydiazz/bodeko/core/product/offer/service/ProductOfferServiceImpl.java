package com.reydiazz.bodeko.core.product.offer.service;

import com.reydiazz.bodeko.core.product.core.model.entity.Product;
import com.reydiazz.bodeko.core.product.core.service.ProductService;
import com.reydiazz.bodeko.core.product.offer.component.ProductOfferMapper;
import com.reydiazz.bodeko.core.product.offer.exception.ProductOfferInvalidDateRangeException;
import com.reydiazz.bodeko.core.product.offer.exception.ProductOfferOverlapException;
import com.reydiazz.bodeko.core.product.offer.model.entity.ProductOffer;
import com.reydiazz.bodeko.core.product.offer.repository.ProductOfferRepository;
import com.reydiazz.bodeko.core.product.offer.web.request.CreateProductOfferRequest;
import com.reydiazz.bodeko.core.product.offer.web.response.ProductOfferResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductOfferServiceImpl implements ProductOfferService {

    private final ProductOfferRepository productOfferRepository;
    private final ProductOfferMapper productOfferMapper;

    private final ProductService productService;

    @Override
    @Transactional(readOnly = true)
    public List<ProductOfferResponse> getAll(UUID productId, UUID storeId) {
        productService.findEntityById(productId, storeId);
        return productOfferRepository.findAllByProductIdOrderByStartsAtDesc(productId).stream()
                .map(productOfferMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public ProductOfferResponse create(UUID productId, UUID storeId, CreateProductOfferRequest request) {
        verifyDateRange(request.startsAt(), request.endsAt());
        Product product = productService.findEntityById(productId, storeId);
        verifyNoOverlap(productId, request.startsAt(), request.endsAt());
        ProductOffer offer = ProductOffer.builder()
                .productId(product.getId())
                .storeId(storeId)
                .product(product)
                .startsAt(request.startsAt())
                .endsAt(request.endsAt())
                .offerPrice(request.offerPrice())
                .createdAt(OffsetDateTime.now())
                .build();
        ProductOffer offerSaved = productOfferRepository.save(offer);
        return productOfferMapper.toResponse(offerSaved);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal resolveEffectivePrice(Product product, OffsetDateTime moment) {
        return productOfferRepository.findActiveByProductId(product.getId(), moment).stream()
                .findFirst()
                .map(ProductOffer::getOfferPrice)
                .orElse(product.getBasePrice());
    }

    @Override
    @Transactional(readOnly = true)
    public Map<UUID, BigDecimal> findActiveOfferPrices(Collection<UUID> productIds, OffsetDateTime moment) {
        if (productIds.isEmpty()) {
            return Map.of();
        }
        return productOfferRepository.findActiveByProductIds(productIds, moment).stream()
                .collect(Collectors.toMap(
                        ProductOffer::getProductId,
                        ProductOffer::getOfferPrice,
                        (latest, older) -> latest
                ));
    }

    private void verifyDateRange(OffsetDateTime startsAt, OffsetDateTime endsAt) {
        if (!endsAt.isAfter(startsAt)) {
            throw new ProductOfferInvalidDateRangeException();
        }
    }

    private void verifyNoOverlap(UUID productId, OffsetDateTime startsAt, OffsetDateTime endsAt) {
        if (productOfferRepository.existsOverlapping(productId, startsAt, endsAt)) {
            throw new ProductOfferOverlapException();
        }
    }

}
