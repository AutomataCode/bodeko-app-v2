package com.reydiazz.bodeko.core.product.variant.service;

import com.github.f4b6a3.uuid.UuidCreator;
import com.reydiazz.bodeko.core.inventory.model.entity.Inventory;
import com.reydiazz.bodeko.core.inventory.repository.InventoryRepository;
import com.reydiazz.bodeko.core.product.core.exception.ProductNotFoundException;
import com.reydiazz.bodeko.core.product.core.model.entity.Product;
import com.reydiazz.bodeko.core.product.core.repository.ProductRepository;
import com.reydiazz.bodeko.core.product.variant.model.ProductVariant;
import com.reydiazz.bodeko.core.product.variant.repository.ProductVariantRepository;
import com.reydiazz.bodeko.core.product.variant.web.CreateProductVariantRequest;
import com.reydiazz.bodeko.core.product.variant.web.ProductVariantResponse;
import com.reydiazz.bodeko.shared.exception.BusinessException;
import com.reydiazz.bodeko.shared.validation.ValidationError;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductVariantService {
    private final ProductVariantRepository variants;
    private final ProductRepository products;
    private final InventoryRepository inventory;

    @Transactional(readOnly = true)
    public List<ProductVariantResponse> list(UUID productId, UUID storeId) {
        products.findByIdAndStoreId(productId, storeId).orElseThrow(ProductNotFoundException::new);
        var found = variants.findAllByProductIdOrderByIsDefaultDescSkuAsc(productId);
        Map<UUID, Integer> quantities = inventory.findAllByVariantIdIn(found.stream().map(ProductVariant::getId).toList()).stream()
                .collect(Collectors.toMap(Inventory::getVariantId, Inventory::getQuantity));
        return found.stream().map(variant -> response(variant, quantities.getOrDefault(variant.getId(), 0))).toList();
    }

    @Transactional
    public ProductVariantResponse create(UUID productId, UUID storeId, CreateProductVariantRequest request) {
        Product product = products.findByIdAndStoreIdForUpdate(productId, storeId).orElseThrow(ProductNotFoundException::new);
        String sku = request.sku().trim();
        Map<String, String> options = normalizeOptions(request.options());
        String key = optionsKey(options);
        if (key.length() > 2000) throw new IllegalArgumentException("Las opciones exceden el tamaño permitido.");
        if (variants.existsByStoreIdAndSkuIgnoreCase(storeId, sku) || variants.existsByProductIdAndOptionsKey(productId, key))
            throw new BusinessException(ValidationError.PRODUCT_VARIANT_DUPLICATE);
        UUID id = UuidCreator.getTimeOrderedEpoch();
        ProductVariant variant;
        try {
            variant = variants.saveAndFlush(ProductVariant.builder().id(id).product(product).productId(productId)
                    .storeId(storeId).sku(sku).options(options).optionsKey(key).basePrice(request.basePrice())
                    .isDefault(false).isActive(true).createdAt(OffsetDateTime.now()).build());
        } catch (DataIntegrityViolationException duplicate) {
            // Los índices únicos también protegen si dos solicitudes llegan al mismo tiempo.
            throw new BusinessException(ValidationError.PRODUCT_VARIANT_DUPLICATE);
        }
        inventory.saveAndFlush(Inventory.builder().id(UuidCreator.getTimeOrderedEpoch()).product(product).variantId(id)
                .quantity(0).updatedAt(OffsetDateTime.now()).build());
        return response(variant, 0);
    }

    /** Actualiza SKU, opciones y precio sin cambiar el ID que usan carrito e historial. */
    @Transactional
    public ProductVariantResponse update(UUID productId, UUID variantId, UUID storeId, CreateProductVariantRequest request) {
        products.findByIdAndStoreIdForUpdate(productId, storeId).orElseThrow(ProductNotFoundException::new);
        ProductVariant variant = variants.findByIdAndStoreIdForUpdate(variantId, storeId)
                .filter(item -> item.getProductId().equals(productId))
                .orElseThrow(ProductNotFoundException::new);
        String sku = request.sku().trim();
        Map<String, String> options = normalizeOptions(request.options());
        String key = optionsKey(options);
        if (key.length() > 2000) throw new IllegalArgumentException("Las opciones exceden el tamaño permitido.");
        if (variants.existsByStoreIdAndSkuIgnoreCaseAndIdNot(storeId, sku, variantId)
                || variants.existsByProductIdAndOptionsKeyAndIdNot(productId, key, variantId)) {
            throw new BusinessException(ValidationError.PRODUCT_VARIANT_DUPLICATE);
        }
        try {
            variant.updateDetails(sku, options, key, request.basePrice());
            variants.flush();
        } catch (DataIntegrityViolationException duplicate) {
            throw new BusinessException(ValidationError.PRODUCT_VARIANT_DUPLICATE);
        }
        int quantity = inventory.findByVariantId(variantId).map(Inventory::getQuantity).orElse(0);
        return response(variant, quantity);
    }

    /** Cambia la disponibilidad de una variante sin borrar su historial. */
    @Transactional
    public ProductVariantResponse changeStatus(UUID productId, UUID variantId, UUID storeId, boolean active) {
        products.findByIdAndStoreIdForUpdate(productId, storeId).orElseThrow(ProductNotFoundException::new);
        ProductVariant variant = variants.findByIdAndStoreIdForUpdate(variantId, storeId)
                .filter(item -> item.getProductId().equals(productId))
                .orElseThrow(ProductNotFoundException::new);
        variant.changeActive(active);
        int quantity = inventory.findByVariantId(variantId).map(Inventory::getQuantity).orElse(0);
        return response(variant, quantity);
    }

    private ProductVariantResponse response(ProductVariant v, int quantity) {
        return new ProductVariantResponse(v.getId(),v.getProductId(),v.getSku(),v.getOptions(),v.getBasePrice(),quantity,v.isDefault(),v.isActive());
    }

    private Map<String, String> normalizeOptions(Map<String, String> source) {
        Map<String, String> options = source.entrySet().stream()
                .sorted(Map.Entry.comparingByKey(String.CASE_INSENSITIVE_ORDER))
                .collect(Collectors.toMap(e -> com.reydiazz.bodeko.shared.validation.NormalizedText.clean(e.getKey()),
                        e -> com.reydiazz.bodeko.shared.validation.NormalizedText.clean(e.getValue()),
                        (first, ignored) -> first, java.util.LinkedHashMap::new));
        if (options.keySet().stream().map(value -> value.toLowerCase(Locale.ROOT)).distinct().count() != options.size())
            throw new BusinessException(ValidationError.PRODUCT_VARIANT_DUPLICATE);
        return options;
    }

    private String optionsKey(Map<String, String> options) {
        return options.entrySet().stream().map(e -> e.getKey().toLowerCase(Locale.ROOT).length()+":"+e.getKey().toLowerCase(Locale.ROOT)
                        +"="+e.getValue().toLowerCase(Locale.ROOT).length()+":"+e.getValue().toLowerCase(Locale.ROOT))
                .collect(Collectors.joining(";"));
    }
}
