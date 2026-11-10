package com.reydiazz.bodeko.core.shopping.cart.util;

import com.reydiazz.bodeko.security.authorization.util.CustomerRoutes;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ShoppingCartRouters {

    public final String API_BASE = CustomerRoutes.API_BASE + "/stores/{storeId}/cart";
    public final String ITEMS = "/items";
    public final String ITEM_ID = "/items/{productId}";

}
