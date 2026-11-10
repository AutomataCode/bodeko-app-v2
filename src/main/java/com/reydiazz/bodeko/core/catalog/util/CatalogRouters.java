package com.reydiazz.bodeko.core.catalog.util;

import com.reydiazz.bodeko.security.authorization.util.PublicRoutes;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CatalogRouters {

    public final String API_BASE = PublicRoutes.API_BASE + "/stores/{storeId}/products";

}
