package com.reydiazz.bodeko.core.order.customer.web.response;

import com.reydiazz.bodeko.core.order.core.web.response.OrderResponse;
import com.reydiazz.bodeko.core.store.model.enums.TypeCurrency;

/** Detalle guardado de la orden y datos de la tienda para continuar el pago. */
public record CustomerOrderDetailResponse(
        OrderResponse order, String storeName, String storeSubdomain, TypeCurrency currency
) {}
