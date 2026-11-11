package com.reydiazz.bodeko.core.product.offer.repository;

import com.reydiazz.bodeko.core.product.offer.model.entity.ProductOffer;
import com.reydiazz.bodeko.core.product.offer.model.entity.ProductOfferId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface ProductOfferRepository extends JpaRepository<ProductOffer, ProductOfferId> {

    List<ProductOffer> findAllByProductIdOrderByStartsAtDesc(UUID productId);

    @Query("select count(o) > 0 from ProductOffer o "
            + "where o.productId = :productId and o.startsAt < :endsAt and o.endsAt > :startsAt")
    boolean existsOverlapping(
            @Param("productId") UUID productId,
            @Param("startsAt") OffsetDateTime startsAt,
            @Param("endsAt") OffsetDateTime endsAt);

    @org.springframework.data.jpa.repository.EntityGraph(attributePaths = "product")
    @Query("select o from ProductOffer o "
            + "where o.productId = :productId and o.startsAt <= :moment and o.endsAt > :moment "
            + "order by o.createdAt desc")
    List<ProductOffer> findActiveByProductId(
            @Param("productId") UUID productId,
            @Param("moment") OffsetDateTime moment);

    @org.springframework.data.jpa.repository.EntityGraph(attributePaths = "product")
    @Query("select o from ProductOffer o "
            + "where o.productId in :productIds and o.startsAt <= :moment and o.endsAt > :moment "
            + "order by o.createdAt desc")
    List<ProductOffer> findActiveByProductIds(
            @Param("productIds") Collection<UUID> productIds,
            @Param("moment") OffsetDateTime moment);

}
