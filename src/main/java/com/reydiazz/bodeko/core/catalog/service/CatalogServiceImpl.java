package com.reydiazz.bodeko.core.catalog.service;

import com.reydiazz.bodeko.core.catalog.component.CatalogMapper;
import com.reydiazz.bodeko.core.catalog.repository.ProductCatalogSpecifications;
import com.reydiazz.bodeko.core.catalog.web.response.CatalogProductResponse;
import com.reydiazz.bodeko.core.inventory.service.InventoryService;
import com.reydiazz.bodeko.core.product.core.model.entity.Product;
import com.reydiazz.bodeko.core.product.core.repository.ProductRepository;
import com.reydiazz.bodeko.core.product.offer.service.ProductOfferService;
import com.reydiazz.bodeko.core.store.service.StoreService;
import com.reydiazz.bodeko.shared.response.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CatalogServiceImpl implements CatalogService {

    private static final int MAX_PAGE_SIZE = 100;

    private final ProductRepository productRepository;
    private final CatalogMapper catalogMapper;

    private final StoreService storeService;
    private final ProductOfferService productOfferService;
    private final InventoryService inventoryService;
    private final com.reydiazz.bodeko.core.product.variant.repository.ProductVariantRepository variantRepository;
    private final com.reydiazz.bodeko.core.inventory.repository.InventoryRepository inventoryRepository;

    @Override
    @Transactional(readOnly = true)
    public CatalogProductResponse getById(UUID storeId, UUID productId) {
        storeService.findEntityById(storeId);
        Product product = productRepository.findByIdAndStoreId(productId, storeId)
                .filter(item -> item.getStatus() == com.reydiazz.bodeko.core.product.core.model.enums.ProductStatus.ACTIVE)
                .orElseThrow(com.reydiazz.bodeko.core.product.core.exception.ProductNotFoundException::new);
        List<UUID> ids = List.of(productId);
        BigDecimal offer = productOfferService.findActiveOfferPrices(ids, OffsetDateTime.now()).get(productId);
        BigDecimal ratio = offer == null ? null : offer.divide(product.getBasePrice(), 6, java.math.RoundingMode.HALF_UP);
        var productVariants = variantRepository.findAllByProductIdOrderByIsDefaultDescSkuAsc(productId).stream()
                .filter(com.reydiazz.bodeko.core.product.variant.model.ProductVariant::isActive).toList();
        Map<UUID, Integer> stockByVariant = inventoryRepository.findAllByVariantIdIn(
                        productVariants.stream().map(com.reydiazz.bodeko.core.product.variant.model.ProductVariant::getId).toList())
                .stream().collect(java.util.stream.Collectors.toMap(com.reydiazz.bodeko.core.inventory.model.entity.Inventory::getVariantId,
                        com.reydiazz.bodeko.core.inventory.model.entity.Inventory::getQuantity));
        List<com.reydiazz.bodeko.core.catalog.web.response.CatalogVariantResponse> variants = productVariants.stream()
                .map(variant -> {
                    int quantity = stockByVariant.getOrDefault(variant.getId(), 0);
                    BigDecimal variantOffer = ratio == null ? null : variant.getBasePrice().multiply(ratio).setScale(2, java.math.RoundingMode.HALF_UP);
                    return new com.reydiazz.bodeko.core.catalog.web.response.CatalogVariantResponse(variant.getId(), variant.getSku(), variant.getOptions(), variant.getBasePrice(), variantOffer, quantity);
                }).toList();
        return catalogMapper.toResponse(product, offer, variants.stream().anyMatch(v -> v.quantity() > 0), variants);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<CatalogProductResponse> search(
            UUID storeId,
            UUID categoryId,
            String search,
            Boolean inStock,
            int page,
            int size
    ) {
        storeService.findEntityById(storeId);
        Specification<Product> specification = buildSpecification(storeId, categoryId, search, inStock);
        Page<Product> products = productRepository.findAll(specification, buildPageable(page, size));
        List<UUID> productIds = products.getContent().stream().map(Product::getId).toList();
        Map<UUID, BigDecimal> offerPrices = productOfferService.findActiveOfferPrices(productIds, OffsetDateTime.now());
        Map<UUID, Integer> quantities = inventoryService.findQuantitiesByProductIds(productIds);
        Map<UUID, List<com.reydiazz.bodeko.core.product.variant.model.ProductVariant>> variantsByProduct =
                variantRepository.findAllByProductIdInAndIsActiveTrue(productIds).stream()
                        .collect(java.util.stream.Collectors.groupingBy(com.reydiazz.bodeko.core.product.variant.model.ProductVariant::getProductId));
        List<CatalogProductResponse> content = products.getContent().stream()
                .map(product -> {
                    List<com.reydiazz.bodeko.core.product.variant.model.ProductVariant> activeVariants =
                            variantsByProduct.getOrDefault(product.getId(), List.of());
                    BigDecimal startingPrice = activeVariants.stream()
                            .map(com.reydiazz.bodeko.core.product.variant.model.ProductVariant::getBasePrice)
                            .min(BigDecimal::compareTo).orElse(product.getBasePrice());
                    BigDecimal offer = offerPrices.get(product.getId());
                    BigDecimal startingOffer = offer == null ? null : activeVariants.stream()
                            .map(variant -> variant.getBasePrice().multiply(offer)
                                    .divide(product.getBasePrice(), 2, java.math.RoundingMode.HALF_UP))
                            .min(BigDecimal::compareTo).orElse(offer);
                    return catalogMapper.toResponse(product, startingPrice, startingOffer,
                            quantities.getOrDefault(product.getId(), 0) > 0);
                })
                .toList();
        return new PageResponse<>(
                content,
                products.getNumber(),
                products.getSize(),
                products.getTotalElements(),
                products.getTotalPages()
        );
    }

    private Specification<Product> buildSpecification(UUID storeId, UUID categoryId, String search, Boolean inStock) {
        Specification<Product> specification = ProductCatalogSpecifications.activeInStore(storeId);
        if (categoryId != null) {
            specification = specification.and(ProductCatalogSpecifications.inCategory(categoryId));
        }
        if (search != null && !search.isBlank()) {
            specification = specification.and(ProductCatalogSpecifications.nameContains(search));
        }
        if (Boolean.TRUE.equals(inStock)) {
            specification = specification.and(ProductCatalogSpecifications.withStock());
        }
        return specification;
    }

    private Pageable buildPageable(int page, int size) {
        int safePage = Math.max(page, 0);
        int safeSize = Math.min(Math.max(size, 1), MAX_PAGE_SIZE);
        return PageRequest.of(safePage, safeSize, Sort.by(Sort.Direction.DESC, "createdAt"));
    }

}
