package com.reydiazz.bodeko.core.shopping.cart.repository;

import com.reydiazz.bodeko.core.shopping.cart.model.entity.ShoppingCart;
import com.reydiazz.bodeko.core.shopping.cart.model.enums.ShoppingCartStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, UUID> {

    Optional<ShoppingCart> findByUserIdAndStoreIdAndStatus(UUID userId, UUID storeId, ShoppingCartStatus status);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select c from ShoppingCart c "
            + "where c.user.id = :userId and c.store.id = :storeId and c.status = :status")
    Optional<ShoppingCart> findByUserIdAndStoreIdAndStatusForUpdate(
            @Param("userId") UUID userId,
            @Param("storeId") UUID storeId,
            @Param("status") ShoppingCartStatus status);

}
