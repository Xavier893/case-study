package com.xavier.client_backend.mappers.impl;

import com.xavier.client_backend.domain.dto.OrderDto;
import com.xavier.client_backend.domain.dto.OrderItemDto;
import com.xavier.client_backend.domain.entities.OrderEntity;
import com.xavier.client_backend.domain.entities.OrderItemEntity;
import com.xavier.client_backend.domain.entities.ClientEntity;
import com.xavier.client_backend.mappers.Mapper;
import com.xavier.client_backend.repositories.ClientRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class OrderMapperImpl implements Mapper<OrderEntity, OrderDto> {
    private final ModelMapper modelMapper;
    private final Mapper<OrderItemEntity, OrderItemDto> orderItemMapper;
    private final ClientRepository clientRepository;

    public OrderMapperImpl(ModelMapper modelMapper, Mapper<OrderItemEntity, OrderItemDto> orderItemMapper, ClientRepository clientRepository) {
        this.modelMapper = modelMapper;
        this.orderItemMapper = orderItemMapper;
        this.clientRepository = clientRepository;
    }

    @Override
    public OrderDto mapTo(OrderEntity orderEntity) {
        OrderDto orderDto = modelMapper.map(orderEntity, OrderDto.class);
        orderDto.setClientId(orderEntity.getClient().getId());
        Set<OrderItemDto> orderItems = orderEntity.getOrderItems().stream()
                .map(orderItemMapper::mapTo)
                .collect(Collectors.toSet());
        orderDto.setOrderItems(orderItems);
        return orderDto;
    }

    @Override
    public OrderEntity mapFrom(OrderDto orderDto) {
        OrderEntity orderEntity = modelMapper.map(orderDto, OrderEntity.class);
        ClientEntity client = clientRepository.findById(orderDto.getClientId())
                .orElseThrow(() -> new RuntimeException("Client not found"));
        orderEntity.setClient(client);
        Set<OrderItemEntity> orderItems = orderDto.getOrderItems().stream()
                .map(orderItemMapper::mapFrom)
                .peek(orderItem -> orderItem.setOrder(orderEntity))
                .collect(Collectors.toSet());
        orderEntity.setOrderItems(orderItems);
        return orderEntity;
    }
}
