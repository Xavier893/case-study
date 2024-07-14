package com.xavier.client_backend.services.impl;

import com.xavier.client_backend.domain.entities.OrderEntity;
import com.xavier.client_backend.repositories.OrderRepository;
import com.xavier.client_backend.services.OrderService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Async
    @Override
    public CompletableFuture<OrderEntity> save(OrderEntity orderEntity) {
        return CompletableFuture.completedFuture(orderRepository.save(orderEntity));
    }

    @Async
    @Override
    public CompletableFuture<OrderEntity> updateOrder(Long orderId, OrderEntity updatedOrder) {
        OrderEntity existingOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new NoSuchElementException("Order not found"));

        existingOrder.getOrderItems().clear();
        existingOrder.getOrderItems().addAll(updatedOrder.getOrderItems());
        existingOrder.setOrderDate(updatedOrder.getOrderDate());
        return CompletableFuture.completedFuture(orderRepository.save(existingOrder));
    }

    @Async
    @Override
    public CompletableFuture<List<OrderEntity>> findAll() {
        return CompletableFuture.completedFuture(
                StreamSupport.stream(orderRepository.findAll().spliterator(), false)
                        .collect(Collectors.toList()));
    }

    @Async
    @Override
    public CompletableFuture<OrderEntity> findById(Long id) {
        return CompletableFuture.completedFuture(orderRepository.findById(id).orElse(null));
    }

    @Async
    @Override
    public CompletableFuture<List<OrderEntity>> findByClientId(Long clientId) {
        return CompletableFuture.completedFuture(orderRepository.findByClient_Id(clientId));
    }

    @Async
    @Override
    public CompletableFuture<Void> deleteById(Long id) {
        orderRepository.deleteById(id);
        return CompletableFuture.completedFuture(null);
    }
}
