package com.reydiazz.bodeko.core.order.customer.service;

import com.reydiazz.bodeko.core.order.core.model.enums.OrderStatus;
import com.reydiazz.bodeko.core.order.customer.web.response.CustomerOrderDetailResponse;
import com.reydiazz.bodeko.core.order.customer.web.response.CustomerOrderSummaryResponse;
import com.reydiazz.bodeko.shared.response.PageResponse;
import java.util.UUID;

/** Consulta y cancelación de las órdenes del cliente autenticado. */
public interface CustomerOrderService {
    PageResponse<CustomerOrderSummaryResponse> list(UUID customerId, OrderStatus status, int page, int size);
    CustomerOrderDetailResponse detail(UUID customerId, UUID orderId);
    CustomerOrderDetailResponse cancel(UUID customerId, UUID orderId);
}
