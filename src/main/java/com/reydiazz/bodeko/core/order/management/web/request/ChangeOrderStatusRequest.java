package com.reydiazz.bodeko.core.order.management.web.request;

import com.reydiazz.bodeko.core.order.core.model.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;

public record ChangeOrderStatusRequest(

        @NotNull
        OrderStatus status

) {
}
