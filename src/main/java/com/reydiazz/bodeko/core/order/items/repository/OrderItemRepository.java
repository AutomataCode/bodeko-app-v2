package com.reydiazz.bodeko.core.order.items.repository;

import com.reydiazz.bodeko.core.order.items.model.entity.OrderItem;
import com.reydiazz.bodeko.core.order.items.model.entity.OrderItemId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OrderItemRepository extends JpaRepository<OrderItem, OrderItemId> {

    List<OrderItem> findAllByOrderIdOrderByProductNameAsc(UUID orderId);

}
