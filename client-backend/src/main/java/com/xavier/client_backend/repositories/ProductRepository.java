package com.xavier.client_backend.repositories;

import com.xavier.client_backend.domain.entities.ProductEntity;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<ProductEntity, Long> {
}
