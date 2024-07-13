package com.xavier.client_backend.services;

import com.xavier.client_backend.domain.entities.ProductEntity;
import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<ProductEntity> findAll();
    Optional<ProductEntity> getProductById(Long id);
}
