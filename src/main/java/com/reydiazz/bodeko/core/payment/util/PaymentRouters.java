package com.reydiazz.bodeko.core.payment.util;

import com.reydiazz.bodeko.security.authorization.util.CustomerRoutes;
import lombok.experimental.UtilityClass;

@UtilityClass
public class PaymentRouters {

    public final String API_BASE = CustomerRoutes.API_BASE + "/stores/{storeId}/orders/{orderId}/payments";

}
