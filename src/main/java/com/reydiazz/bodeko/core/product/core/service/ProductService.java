package com.reydiazz.bodeko.core.product.core.service;

import com.reydiazz.bodeko.core.product.core.model.entity.Product;
import com.reydiazz.bodeko.core.product.core.web.request.ChangeProductStatusRequest;
import com.reydiazz.bodeko.core.product.core.web.request.CreateProductRequest;
import com.reydiazz.bodeko.core.product.core.web.request.EditProductRequest;
import com.reydiazz.bodeko.core.product.core.web.response.ProductResponse;

import java.util.List;
import java.util.UUID;

public interface ProductService {

    List<ProductResponse> getAll(UUID storeId);

    ProductResponse getById(UUID id, UUID storeId);

    ProductResponse create(UUID storeId, CreateProductRequest request);

    ProductResponse update(UUID id, UUID storeId, EditProductRequest request);

    ProductResponse changeStatus(UUID id, UUID storeId, ChangeProductStatusRequest request);

    Product findEntityById(UUID id, UUID storeId);

}
