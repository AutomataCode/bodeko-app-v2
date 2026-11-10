package com.reydiazz.bodeko.core.product.offer.model.entity;

import lombok.*;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ProductOfferId implements Serializable {

    private UUID productId;

    private OffsetDateTime startsAt;

    private OffsetDateTime endsAt;

}
