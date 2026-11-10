package com.reydiazz.bodeko.core.category.web.request;

import jakarta.validation.constraints.NotBlank;

public record EditCategoryRequest(
        @NotBlank(message = "Name is required")
        String name
) {
}
