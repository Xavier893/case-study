package com.xavier.client_backend.services;

import com.xavier.client_backend.domain.entities.OrderEntity;

import java.util.List;

public interface OrderService {
    OrderEntity save(OrderEntity orderEntity);
    OrderEntity updateOrder(Long orderId, OrderEntity updatedOrder);
    List<OrderEntity> findAll();
    OrderEntity findById(Long id);
    List<OrderEntity> findByClientId(Long clientId);
    void deleteById(Long id);
}
