package com.reydiazz.bodeko.core.cart.items.model.entity;

import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ShoppingCartItemId implements Serializable {

    private UUID cartId;
    private UUID productId;
}
