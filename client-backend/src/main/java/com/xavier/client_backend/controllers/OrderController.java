package com.xavier.client_backend.controllers;

import com.xavier.client_backend.domain.dto.OrderDto;
import com.xavier.client_backend.domain.entities.OrderEntity;
import com.xavier.client_backend.mappers.impl.OrderMapperImpl;
import com.xavier.client_backend.services.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@RestController
@CrossOrigin("https://mango-plant-0dc82e003.5.azurestaticapps.net")
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final OrderMapperImpl orderMapper;

    public OrderController(OrderService orderService, OrderMapperImpl orderMapper) {
        this.orderService = orderService;
        this.orderMapper = orderMapper;
    }

    @PostMapping
    public CompletableFuture<ResponseEntity<OrderDto>> createOrder(@RequestBody OrderDto orderDto) {
        OrderEntity orderEntity = orderMapper.mapFrom(orderDto);
        return orderService.save(orderEntity)
                .thenApply(savedOrder -> ResponseEntity.ok(orderMapper.mapTo(savedOrder)));
    }

    @PutMapping("/{orderId}")
    public CompletableFuture<ResponseEntity<OrderDto>> updateOrder(@PathVariable Long orderId,
            @RequestBody OrderDto orderDto) {
        OrderEntity updatedOrder = orderMapper.mapFrom(orderDto);
        return orderService.updateOrder(orderId, updatedOrder)
                .thenApply(savedOrder -> ResponseEntity.ok(orderMapper.mapTo(savedOrder)));
    }

    @GetMapping
    public CompletableFuture<ResponseEntity<List<OrderDto>>> getAllOrders() {
        return orderService.findAll()
                .thenApply(orders -> ResponseEntity.ok(
                        orders.stream().map(orderMapper::mapTo).collect(Collectors.toList())));
    }

    @GetMapping("/{orderId}")
    public CompletableFuture<ResponseEntity<OrderDto>> getOrderById(@PathVariable Long orderId) {
        return orderService.findById(orderId)
                .thenApply(order -> ResponseEntity.ok(orderMapper.mapTo(order)));
    }

    @GetMapping("/client/{clientId}")
    public CompletableFuture<ResponseEntity<List<OrderDto>>> getOrdersByClientId(@PathVariable Long clientId) {
        return orderService.findByClientId(clientId)
                .thenApply(orders -> ResponseEntity.ok(
                        orders.stream().map(orderMapper::mapTo).collect(Collectors.toList())));
    }

    @DeleteMapping("/{orderId}")
    public CompletableFuture<ResponseEntity<Void>> deleteOrderById(@PathVariable Long orderId) {
        return orderService.deleteById(orderId)
                .thenApply(aVoid -> ResponseEntity.noContent().build());
    }
}
