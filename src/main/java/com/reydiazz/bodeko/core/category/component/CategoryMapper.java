package com.reydiazz.bodeko.core.category.component;

import com.reydiazz.bodeko.core.category.model.Category;
import com.reydiazz.bodeko.core.category.web.request.CreateCategoryRequest;
import com.reydiazz.bodeko.core.category.web.response.CategoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public CategoryResponse toResponse(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getName()
        );
    }

}
