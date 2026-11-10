package com.reydiazz.bodeko.core.category.web.controller;

import com.reydiazz.bodeko.core.category.service.CategoryService;
import com.reydiazz.bodeko.core.category.util.CategoryRouters;
import com.reydiazz.bodeko.core.category.web.request.CreateCategoryRequest;
import com.reydiazz.bodeko.core.category.web.request.EditCategoryRequest;
import com.reydiazz.bodeko.core.category.web.response.CategoryResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(CategoryRouters.API_BASE)
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryResponse> create(@PathVariable UUID storeId, @Valid @RequestBody CreateCategoryRequest request) {
        CategoryResponse response = categoryService.create(storeId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAll(@PathVariable UUID storeId) {
        List<CategoryResponse> response = categoryService.getAll(storeId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryResponse> update(
            @PathVariable UUID storeId,
            @PathVariable UUID categoryId,
            @Valid @RequestBody EditCategoryRequest request) {
        CategoryResponse response = categoryService.update(categoryId, storeId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> delete(
            @PathVariable UUID storeId,
            @PathVariable UUID categoryId) {
        categoryService.delete(categoryId, storeId);
        return ResponseEntity.noContent().build();
    }
}
