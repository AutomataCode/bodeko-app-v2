package com.reydiazz.bodeko.core.order.management.component;

import com.reydiazz.bodeko.core.order.core.model.entity.Order;
import com.reydiazz.bodeko.core.order.management.web.response.OrderSummaryResponse;
import org.springframework.stereotype.Component;

@Component
public class OrderManagementMapper {

    public OrderSummaryResponse toSummary(Order order) {
        return new OrderSummaryResponse(
                order.getId(),
                order.getOrderNumber(),
                order.getInvoiceType(),
                order.getCustomerDocumentType(),
                order.getCustomerDocumentNumber(),
                order.getCustomerBusinessName(),
                order.getTotal(),
                order.getStatus(),
                order.getCreatedAt()
        );
    }

}
