package com.reydiazz.bodeko.core.inventory.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class InventoryRouters {

    public final String API_BASE = "/api/stores/{storeId}/products/{productId}/inventory";
    public final String QUANTITY = "/quantity";
    public final String LOW_STOCK_THRESHOLD = "/low-stock-threshold";

}
