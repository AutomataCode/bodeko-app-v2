package com.reydiazz.bodeko.core.order.tracking;

import com.reydiazz.bodeko.core.order.core.component.OrderMapper;
import com.reydiazz.bodeko.core.order.core.model.enums.OrderStatus;
import com.reydiazz.bodeko.core.order.core.repository.OrderRepository;
import com.reydiazz.bodeko.core.order.core.web.response.OrderResponse;
import com.reydiazz.bodeko.core.order.items.repository.OrderItemRepository;
import com.reydiazz.bodeko.core.store.exception.StoreNotFoundException;
import com.reydiazz.bodeko.core.store.repository.StoreRepository;
import com.reydiazz.bodeko.shared.exception.BusinessException;
import com.reydiazz.bodeko.shared.validation.ValidationError;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

/** Actualiza el seguimiento de órdenes enviadas de una tienda. */
@Service
@RequiredArgsConstructor
public class OrderTrackingService {
    private final OrderRepository orders;
    private final OrderItemRepository items;
    private final OrderMapper mapper;
    private final StoreRepository stores;

    @Transactional
    public OrderResponse update(UUID storeId, UUID ownerId, UUID orderId, UpdateTrackingRequest request) {
        if (!stores.existsByIdAndOwnerId(storeId, ownerId)) throw new StoreNotFoundException();
        var order = orders.findByIdAndStoreIdForUpdate(orderId, storeId)
                .orElseThrow(() -> new BusinessException(ValidationError.ORDER_NOT_FOUND));
        if (order.getStatus() != OrderStatus.SHIPPED)
            throw new BusinessException(ValidationError.ORDER_TRACKING_REQUIRES_SHIPPED);
        order.updateTracking(request.carrier().trim(), request.trackingNumber().trim(),
                request.trackingUrl() == null || request.trackingUrl().isBlank() ? null : request.trackingUrl().trim(),
                request.estimatedDeliveryAt());
        return mapper.toResponse(order, items.findAllByOrderIdOrderByProductNameAsc(orderId));
    }
}
