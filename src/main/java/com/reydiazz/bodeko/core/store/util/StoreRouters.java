package com.reydiazz.bodeko.core.store.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class StoreRouters {

    public final String API_BASE = "/api/stores";
    public final String STORE_ID = "/{storeId}";
    public final String TAX_RATE = "/{storeId}/tax-rate";

}
