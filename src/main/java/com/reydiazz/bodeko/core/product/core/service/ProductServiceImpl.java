package com.reydiazz.bodeko.core.product.core.service;

import com.github.f4b6a3.uuid.UuidCreator;
import com.reydiazz.bodeko.core.category.model.Category;
import com.reydiazz.bodeko.core.category.service.CategoryService;
import com.reydiazz.bodeko.core.inventory.service.InventoryService;
import com.reydiazz.bodeko.core.product.core.component.ProductMapper;
import com.reydiazz.bodeko.core.product.core.exception.ProductNotFoundException;
import com.reydiazz.bodeko.core.product.core.model.entity.Product;
import com.reydiazz.bodeko.core.product.core.repository.ProductRepository;
import com.reydiazz.bodeko.core.product.core.web.request.ChangeProductStatusRequest;
import com.reydiazz.bodeko.core.product.core.web.request.CreateProductRequest;
import com.reydiazz.bodeko.core.product.core.web.request.EditProductRequest;
import com.reydiazz.bodeko.core.product.core.web.response.ProductResponse;
import com.reydiazz.bodeko.core.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    private final StoreService storeService;
    private final CategoryService categoryService;
    private final InventoryService inventoryService;

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getAll(UUID storeId) {
        return productRepository.findAllByStoreIdOrderByCreatedAtDesc(storeId).stream()
                .map(productMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getById(UUID id, UUID storeId) {
        Product product = findEntityById(id, storeId);
        return productMapper.toResponse(product);
    }

    @Override
    @Transactional
    public ProductResponse create(UUID storeId, CreateProductRequest request) {
        Product product = Product.builder()
                .id(UuidCreator.getTimeOrderedEpoch())
                .store(storeService.getReferenceById(storeId))
                .category(resolveCategory(request.categoryId(), storeId))
                .name(request.name().trim())
                .description(sanitize(request.description()))
                .basePrice(request.basePrice())
                .imageUrl(sanitize(request.imageUrl()))
                .isTaxExempt(request.taxExempt())
                .createdAt(OffsetDateTime.now())
                .build();
        Product productSaved = productRepository.save(product);
        inventoryService.createForProduct(productSaved);
        return productMapper.toResponse(productSaved);
    }

    @Override
    @Transactional
    public ProductResponse update(UUID id, UUID storeId, EditProductRequest request) {
        Product product = findEntityById(id, storeId);
        product.update(
                request.name().trim(),
                sanitize(request.description()),
                request.basePrice(),
                sanitize(request.imageUrl()),
                request.taxExempt(),
                resolveCategory(request.categoryId(), storeId)
        );
        return productMapper.toResponse(product);
    }

    @Override
    @Transactional
    public ProductResponse changeStatus(UUID id, UUID storeId, ChangeProductStatusRequest request) {
        Product product = findEntityById(id, storeId);
        product.changeStatus(request.status());
        return productMapper.toResponse(product);
    }

    @Override
    @Transactional(readOnly = true)
    public Product findEntityById(UUID id, UUID storeId) {
        return productRepository.findByIdAndStoreId(id, storeId)
                .orElseThrow(ProductNotFoundException::new);
    }

    private Category resolveCategory(UUID categoryId, UUID storeId) {
        if (categoryId == null) {
            return null;
        }
        return categoryService.findEntityById(categoryId, storeId);
    }

    private String sanitize(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

}
