package com.reydiazz.bodeko.core.order.core.service;

import com.reydiazz.bodeko.core.order.core.web.request.CheckoutRequest;
import com.reydiazz.bodeko.core.order.core.web.response.OrderResponse;

import java.util.UUID;

public interface OrderService {

    OrderResponse getByCustomer(UUID userId, UUID storeId, UUID orderId);

    OrderResponse checkout(UUID userId, UUID storeId, CheckoutRequest request);

}
