package com.reydiazz.bodeko.core.order.core.repository;

import com.reydiazz.bodeko.core.order.core.model.entity.Order;
import com.reydiazz.bodeko.core.order.core.model.enums.OrderStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID >{

    @EntityGraph(attributePaths = "store")
    Page<Order> findAllByCustomerId(UUID customerId, Pageable pageable);

    @EntityGraph(attributePaths = "store")
    Page<Order> findAllByCustomerIdAndStatus(UUID customerId, OrderStatus status, Pageable pageable);

    @EntityGraph(attributePaths = "store")
    Optional<Order> findByIdAndCustomerId(UUID id, UUID customerId);

    /** Busca la orden del cliente y la bloquea mientras se actualiza. */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select o from Order o where o.id = :id and o.customer.id = :customerId")
    Optional<Order> findByIdAndCustomerIdForUpdate(@Param("id") UUID id, @Param("customerId") UUID customerId);

    Optional<Order> findByIdAndStoreIdAndCustomerId(UUID id, UUID storeId, UUID customerId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select o from Order o "
            + "where o.id = :id and o.store.id = :storeId and o.customer.id = :customerId")
    Optional<Order> findByIdAndStoreIdAndCustomerIdForUpdate(
            @Param("id") UUID id,
            @Param("storeId") UUID storeId,
            @Param("customerId") UUID customerId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select o from Order o where o.id = :id and o.store.id = :storeId")
    Optional<Order> findByIdAndStoreIdForUpdate(
            @Param("id") UUID id,
            @Param("storeId") UUID storeId);

    Optional<Order> findByIdAndStoreId(UUID id, UUID storeId);

    Page<Order> findAllByStoreId(UUID storeId, Pageable pageable);

    Page<Order> findAllByStoreIdAndStatus(UUID storeId, OrderStatus status, Pageable pageable);

}
