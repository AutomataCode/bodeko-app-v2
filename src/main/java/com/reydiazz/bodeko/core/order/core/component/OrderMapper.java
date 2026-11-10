package com.reydiazz.bodeko.core.order.core.component;

import com.reydiazz.bodeko.core.order.core.model.entity.Order;
import com.reydiazz.bodeko.core.order.core.web.response.OrderItemResponse;
import com.reydiazz.bodeko.core.order.core.web.response.OrderResponse;
import com.reydiazz.bodeko.core.order.items.model.entity.OrderItem;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderMapper {

    public OrderResponse toResponse(Order order, List<OrderItem> items) {
        return new OrderResponse(
                order.getId(),
                order.getOrderNumber(),
                order.getStore().getId(),
                order.getInvoiceType(),
                order.getCustomerDocumentType(),
                order.getCustomerDocumentNumber(),
                order.getCustomerBusinessName(),
                order.getCustomerBillingAddress(),
                order.getSubtotal(),
                order.getTaxAmount(),
                order.getTotal(),
                order.getAppliedTaxRate(),
                order.getStatus(),
                order.getCreatedAt(),
                items.stream().map(this::toItemResponse).toList()
        );
    }

    private OrderItemResponse toItemResponse(OrderItem item) {
        return new OrderItemResponse(
                item.getProductId(),
                item.getProductName(),
                item.getQuantity(),
                item.getListUnitPrice(),
                item.getChargedUnitPrice(),
                item.getItemSubtotal()
        );
    }

}
