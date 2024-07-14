package com.xavier.client_backend.mappers.impl;

import com.xavier.client_backend.domain.dto.OrderItemDto;
import com.xavier.client_backend.domain.entities.OrderItemEntity;
import com.xavier.client_backend.domain.entities.ProductEntity;
import com.xavier.client_backend.mappers.Mapper;
import com.xavier.client_backend.repositories.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class OrderItemMapperImpl implements Mapper<OrderItemEntity, OrderItemDto> {
    private final ModelMapper modelMapper;
    private final ProductRepository productRepository;

    public OrderItemMapperImpl(ModelMapper modelMapper, ProductRepository productRepository) {
        this.modelMapper = modelMapper;
        this.productRepository = productRepository;
    }

    @Override
    public OrderItemDto mapTo(OrderItemEntity orderItemEntity) {
        return modelMapper.map(orderItemEntity, OrderItemDto.class);
    }

    @Override
    public OrderItemEntity mapFrom(OrderItemDto orderItemDto) {
        ProductEntity product = productRepository.findById(orderItemDto.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return OrderItemEntity.builder()
                .product(product)
                .quantity(orderItemDto.getQuantity())
                .build();
    }
}
