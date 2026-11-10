package com.reydiazz.bodeko.core.order.history.repository;

import com.reydiazz.bodeko.core.order.history.model.entity.OrderStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderStatusHistoryRepository extends JpaRepository<OrderStatusHistory, UUID> {
}
