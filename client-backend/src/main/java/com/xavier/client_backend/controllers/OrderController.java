package com.xavier.client_backend.controllers;

import com.xavier.client_backend.domain.dto.OrderDto;
import com.xavier.client_backend.domain.entities.OrderEntity;
import com.xavier.client_backend.mappers.Mapper;
import com.xavier.client_backend.services.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * OrderController class provides REST endpoints for CRUD operations on Orders.
 * It uses OrderService and Mapper to perform the operations.
 *
 * @author Xavier
 */
@RestController
@RequestMapping("/api/orders") // Base URL for all Order endpoints
public class OrderController {

    private final OrderService orderService;
    private final Mapper<OrderEntity, OrderDto> orderMapper;

    /**
     * Constructor for OrderController.
     *
     * @param orderService OrderService instance
     * @param orderMapper Mapper instance for OrderEntity and OrderDto
     */
    public OrderController(OrderService orderService, Mapper<OrderEntity, OrderDto> orderMapper) {
        this.orderService = orderService;
        this.orderMapper = orderMapper;
    }

    /**
     * Create a new Order.
     *
     * @param orderDto OrderDto containing order details
     * @return ResponseEntity with created OrderDto and HTTP status
     */
    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@RequestBody OrderDto orderDto) {
        // Map OrderDto to OrderEntity
        OrderEntity orderEntity = orderMapper.mapFrom(orderDto);
        // Save OrderEntity
        OrderEntity savedOrderEntity = orderService.save(orderEntity);
        // Map OrderEntity to OrderDto and return as response
        return new ResponseEntity<>(orderMapper.mapTo(savedOrderEntity), HttpStatus.CREATED);
    }

    /**
     * Update an existing Order.
     *
     * @param id        Order ID
     * @param orderDto  OrderDto containing updated order details
     * @return ResponseEntity with updated OrderDto and HTTP status
     */
    @PutMapping("/{id}")
    public ResponseEntity<OrderDto> updateOrder(@PathVariable Long id, @RequestBody OrderDto orderDto) {
        // Find existing OrderEntity by ID
        OrderEntity existingOrder = orderService.findById(id);
        if (existingOrder == null) {
            // Return HTTP status if OrderEntity not found
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        // Map OrderDto to OrderEntity
        OrderEntity orderEntity = orderMapper.mapFrom(orderDto);
        // Set ID of OrderEntity to update
        orderEntity.setId(id);
        // Save updated OrderEntity
        OrderEntity updatedOrderEntity = orderService.updateOrder(id, orderEntity);
        // Map updated OrderEntity to OrderDto and return as response
        return new ResponseEntity<>(orderMapper.mapTo(updatedOrderEntity), HttpStatus.OK);
    }

    /**
     * List all Orders.
     *
     * @return ResponseEntity with list of OrderDto and HTTP status
     */
    @GetMapping
    public ResponseEntity<List<OrderDto>> listOrders() {
        // Find all OrderEntity
        List<OrderEntity> orders = orderService.findAll();
        // Map each OrderEntity to OrderDto and collect into list
        List<OrderDto> orderDtos = orders.stream()
                .map(orderMapper::mapTo)
                .collect(Collectors.toList());
        // Return list of OrderDto and HTTP status
        return new ResponseEntity<>(orderDtos, HttpStatus.OK);
    }

    /**
     * Get Order by ID.
     *
     * @param id Order ID
     * @return ResponseEntity with OrderDto and HTTP status
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable Long id) {
        // Find OrderEntity by ID
        OrderEntity order = orderService.findById(id);
        if (order == null) {
            // Return HTTP status if OrderEntity not found
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        // Map OrderEntity to OrderDto and return as response
        OrderDto orderDto = orderMapper.mapTo(order);
        return new ResponseEntity<>(orderDto, HttpStatus.OK);
    }

    /**
     * Get Orders by Client ID.
     *
     * @param clientId Client ID
     * @return ResponseEntity with list of OrderDto and HTTP status
     */
    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<OrderDto>> getOrdersByClientId(@PathVariable Long clientId) {
        // Find OrderEntity by Client ID
        List<OrderEntity> orders = orderService.findByClientId(clientId);
        // Map each OrderEntity to OrderDto and collect into list
        List<OrderDto> orderDtos = orders.stream()
                .map(orderMapper::mapTo)
                .collect(Collectors.toList());
        // Return list of OrderDto and HTTP status
        return new ResponseEntity<>(orderDtos, HttpStatus.OK);
    }

    /**
     * Delete Order by ID.
     *
     * @param id Order ID
     * @return ResponseEntity with HTTP status
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        // Delete OrderEntity by ID
        orderService.deleteById(id);
        // Return HTTP status
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
