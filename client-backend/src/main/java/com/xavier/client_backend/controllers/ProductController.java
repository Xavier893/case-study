package com.xavier.client_backend.controllers;

import com.xavier.client_backend.domain.dto.ProductDto;
import com.xavier.client_backend.domain.entities.ProductEntity;
import com.xavier.client_backend.mappers.Mapper;
import com.xavier.client_backend.services.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ProductController {

    private ProductService productService;

    private Mapper<ProductEntity, ProductDto> productMapper;

    public ProductController(ProductService productService, Mapper<ProductEntity, ProductDto> productMapper) {
        this.productService = productService;
        this.productMapper = productMapper;
    }

    @PostMapping(path = "/products")
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto product) {
        ProductEntity productEntity = productMapper.mapFrom(product);
        ProductEntity savedProductEntity = productService.save(productEntity);
        return new ResponseEntity<>(productMapper.mapTo(savedProductEntity), HttpStatus.CREATED);
    }

    @GetMapping(path = "/products")
    public List<ProductDto> listProducts() {
        List<ProductEntity> products = productService.findAll();
        return products.stream().map(productMapper::mapTo).collect(Collectors.toList());
    }
}
