package com.reydiazz.bodeko.core.order.management.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class OrderManagementRouters {

    public final String API_BASE = "/api/stores/{storeId}/orders";
    public final String ORDER_ID = "/{orderId}";
    public final String STATUS = "/{orderId}/status";

}
