package com.reydiazz.bodeko.security.authorization.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class CustomerRoutes {

    public final String API_BASE = "/api/customer";
    public final String PATTERN = API_BASE + "/**";
    public final String STORE_PATTERN = API_BASE + "/stores/{" + StoreScopeRoutes.STORE_ID_VARIABLE + "}/**";

}
