package com.reydiazz.bodeko.core.product.offer.model.entity;

import com.reydiazz.bodeko.core.product.core.model.entity.Product;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Builder
@Entity
@Getter
@IdClass(ProductOfferId.class)
@Table(
        name = "product_offers",
        indexes = {
                @Index(name = "idx_product_offers_store_id", columnList = "store_id")
        }
)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductOffer {

    @Id
    @Column(name = "product_id", nullable = false, updatable = false)
    private UUID productId;

    @Id
    @Column(name = "starts_at", nullable = false, updatable = false)
    private OffsetDateTime startsAt;

    @Id
    @Column(name = "ends_at", nullable = false, updatable = false)
    private OffsetDateTime endsAt;

    @Column(name = "store_id", nullable = false, updatable = false)
    private UUID storeId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumns({
            @JoinColumn(
                    name = "product_id",
                    referencedColumnName = "id",
                    insertable = false,
                    updatable = false
            ),
            @JoinColumn(
                    name = "store_id",
                    referencedColumnName = "store_id",
                    insertable = false,
                    updatable = false
            )
    })
    private Product product;

    @Column(name = "offer_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal offerPrice;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    public boolean isActiveAt(OffsetDateTime moment) {
        return !moment.isBefore(startsAt) && moment.isBefore(endsAt);
    }

}
