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

    @org.springframework.data.jpa.repository.Query(value = "select c.id from shopping_carts c join customers u on u.id=c.user_id where c.status='OPEN' and c.updated_at <= :cutoff and c.recovery_email_sent_at is null and u.marketing_consent=true and exists (select 1 from shopping_cart_items i where i.cart_id=c.id) order by c.updated_at asc limit 100", nativeQuery = true)
    java.util.List<UUID> findRecoverableCartIds(@org.springframework.data.repository.query.Param("cutoff") java.time.OffsetDateTime cutoff);

    @org.springframework.data.jpa.repository.Lock(jakarta.persistence.LockModeType.PESSIMISTIC_WRITE)
    @org.springframework.data.jpa.repository.Query("select c from ShoppingCart c where c.id = :id and c.status = com.reydiazz.bodeko.core.shopping.cart.model.enums.ShoppingCartStatus.OPEN and c.recoveryEmailSentAt is null")
    java.util.Optional<ShoppingCart> findByIdForRecovery(@org.springframework.data.repository.query.Param("id") UUID id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select c from ShoppingCart c "
            + "where c.user.id = :userId and c.store.id = :storeId and c.status = :status")
    Optional<ShoppingCart> findByUserIdAndStoreIdAndStatusForUpdate(
            @Param("userId") UUID userId,
            @Param("storeId") UUID storeId,
            @Param("status") ShoppingCartStatus status);

}
