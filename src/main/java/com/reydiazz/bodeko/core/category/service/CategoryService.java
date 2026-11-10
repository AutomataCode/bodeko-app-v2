package com.reydiazz.bodeko.core.category.service;

import com.reydiazz.bodeko.core.category.model.Category;
import com.reydiazz.bodeko.core.category.web.request.CreateCategoryRequest;
import com.reydiazz.bodeko.core.category.web.request.EditCategoryRequest;
import com.reydiazz.bodeko.core.category.web.response.CategoryResponse;

import java.util.List;
import java.util.UUID;

public interface CategoryService {

    List<CategoryResponse> getAll(UUID storeId);

    CategoryResponse create(UUID storeId,CreateCategoryRequest request);

    CategoryResponse update(UUID id,UUID storeId, EditCategoryRequest request);

    void delete(UUID id, UUID storeId);

    Category findEntityById(UUID id, UUID storeId);

}
