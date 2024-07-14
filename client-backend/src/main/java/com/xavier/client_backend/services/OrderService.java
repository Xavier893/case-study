package com.xavier.client_backend.services;

import com.xavier.client_backend.domain.entities.OrderEntity;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface OrderService {
    CompletableFuture<OrderEntity> save(OrderEntity orderEntity);
    CompletableFuture<OrderEntity> updateOrder(Long orderId, OrderEntity updatedOrder);
    CompletableFuture<List<OrderEntity>> findAll();
    CompletableFuture<OrderEntity> findById(Long id);
    CompletableFuture<List<OrderEntity>> findByClientId(Long clientId);
    CompletableFuture<Void> deleteById(Long id);
}
