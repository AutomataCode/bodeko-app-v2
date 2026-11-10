package com.reydiazz.bodeko.core.cart.items.repository;

import com.reydiazz.bodeko.core.cart.items.model.entity.ShoppingCartItem;
import com.reydiazz.bodeko.core.cart.items.model.entity.ShoppingCartItemId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ShoppingCartItemRepository extends JpaRepository<ShoppingCartItem, ShoppingCartItemId> {

    Optional<ShoppingCartItem> findByCartIdAndProductId(UUID cartId, UUID productId);

    @Query("select i from ShoppingCartItem i join fetch i.product p where i.cartId = :cartId order by p.name asc")
    List<ShoppingCartItem> findAllWithProductByCartId(@Param("cartId") UUID cartId);

}
