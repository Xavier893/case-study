package com.xavier.client_backend.services.impl;

import com.xavier.client_backend.domain.entities.OrderEntity;
import com.xavier.client_backend.repositories.OrderRepository;
import com.xavier.client_backend.services.OrderService;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public OrderEntity save(OrderEntity orderEntity) {
        return orderRepository.save(orderEntity);
    }

    @Override
    public OrderEntity updateOrder(Long orderId, OrderEntity updatedOrder) {
        OrderEntity existingOrder = orderRepository.findById(orderId).orElseThrow(() -> new NoSuchElementException("Order not found"));

        existingOrder.getOrderItems().clear();
        existingOrder.getOrderItems().addAll(updatedOrder.getOrderItems());
        existingOrder.setOrderDate(updatedOrder.getOrderDate());
        return orderRepository.save(existingOrder);
    }

    @Override
    public List<OrderEntity> findAll() {
        return StreamSupport.stream(orderRepository.findAll().spliterator(), false).collect(Collectors.toList());
    }

    @Override
    public OrderEntity findById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    @Override
    public List<OrderEntity> findByClientId(Long clientId) {
        return orderRepository.findByClient_Id(clientId);
    }

    @Override
    public void deleteById(Long id) {
        orderRepository.deleteById(id);
    }
}
