package com.reydiazz.bodeko.core.order.core.util;

import com.reydiazz.bodeko.security.authorization.util.CustomerRoutes;
import lombok.experimental.UtilityClass;

@UtilityClass
public class OrderRouters {

    public final String API_BASE = CustomerRoutes.API_BASE + "/stores/{storeId}/orders";

}
