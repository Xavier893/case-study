package com.xavier.client_backend.repositories;

import com.xavier.client_backend.domain.entities.OrderEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrderRepository extends CrudRepository<OrderEntity, Long> {
    List<OrderEntity> findByClient_Id(Long clientId);
}
