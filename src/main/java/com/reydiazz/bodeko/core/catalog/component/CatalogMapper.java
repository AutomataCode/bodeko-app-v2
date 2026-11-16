package com.reydiazz.bodeko.core.catalog.component;

import com.reydiazz.bodeko.core.catalog.web.response.CatalogProductResponse;
import com.reydiazz.bodeko.core.product.core.model.entity.Product;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class CatalogMapper {

    public CatalogProductResponse toResponse(Product product, BigDecimal offerPrice, boolean inStock) {
        return new CatalogProductResponse(
                product.getId(),
                product.getCategory() != null ? product.getCategory().getId() : null,
                product.getName(),
                product.getDescription(),
                product.getImageUrl(),
                product.getBasePrice(),
                offerPrice,
                inStock,
                List.of()
        );
    }

    /** Usa los precios mínimos de las variantes activas para representar el producto agrupado. */
    public CatalogProductResponse toResponse(Product product, BigDecimal startingPrice,
                                             BigDecimal startingOfferPrice, boolean inStock) {
        return new CatalogProductResponse(product.getId(), product.getCategory() != null ? product.getCategory().getId() : null,
                product.getName(), product.getDescription(), product.getImageUrl(), startingPrice,
                startingOfferPrice, inStock, List.of());
    }

    public CatalogProductResponse toResponse(Product product, BigDecimal offerPrice, boolean inStock,
                                              List<com.reydiazz.bodeko.core.catalog.web.response.CatalogVariantResponse> variants) {
        return new CatalogProductResponse(product.getId(), product.getCategory() != null ? product.getCategory().getId() : null,
                product.getName(), product.getDescription(), product.getImageUrl(), product.getBasePrice(), offerPrice, inStock, variants);
    }

}
