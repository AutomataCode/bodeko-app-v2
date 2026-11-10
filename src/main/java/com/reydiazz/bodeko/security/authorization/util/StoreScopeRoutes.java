package com.reydiazz.bodeko.security.authorization.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class StoreScopeRoutes {

    public final String STORE_ID_VARIABLE = "storeId";
    public final String PATTERN = "/api/stores/{" + STORE_ID_VARIABLE + "}/**";

}
