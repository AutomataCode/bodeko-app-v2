package com.reydiazz.bodeko.core.product.core.web.request;

import com.reydiazz.bodeko.core.product.core.model.enums.ProductStatus;
import jakarta.validation.constraints.NotNull;

public record ChangeProductStatusRequest(

        @NotNull(message = "Status is required")
        ProductStatus status

) {
}
