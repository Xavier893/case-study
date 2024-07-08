package com.xavier.client_backend.services;

import com.xavier.client_backend.domain.entities.ProductEntity;

import java.util.List;

public interface ProductService {
    ProductEntity save(ProductEntity productEntity);

    List<ProductEntity> findAll();
}
