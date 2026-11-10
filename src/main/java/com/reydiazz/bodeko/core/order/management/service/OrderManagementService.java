package com.reydiazz.bodeko.core.order.management.service;

import com.reydiazz.bodeko.core.order.core.model.enums.OrderStatus;
import com.reydiazz.bodeko.core.order.core.web.response.OrderResponse;
import com.reydiazz.bodeko.core.order.management.web.request.ChangeOrderStatusRequest;
import com.reydiazz.bodeko.core.order.management.web.response.OrderSummaryResponse;
import com.reydiazz.bodeko.shared.response.PageResponse;

import java.util.UUID;

public interface OrderManagementService {

    PageResponse<OrderSummaryResponse> getAll(UUID storeId, OrderStatus status, int page, int size);

    OrderResponse getById(UUID orderId, UUID storeId);

    OrderResponse changeStatus(UUID orderId, UUID storeId, UUID adminId, ChangeOrderStatusRequest request);

}
