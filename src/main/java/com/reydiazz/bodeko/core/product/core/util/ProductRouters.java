package com.reydiazz.bodeko.core.product.core.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ProductRouters {

    public final String API_BASE = "/api/stores/{storeId}/products";
    public final String PRODUCT_ID = "/{productId}";
    public final String STATUS = "/{productId}/status";

}
