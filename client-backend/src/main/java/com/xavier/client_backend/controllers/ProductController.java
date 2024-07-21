package com.xavier.client_backend.controllers;

import com.xavier.client_backend.domain.dto.ProductDto;
import com.xavier.client_backend.domain.entities.ProductEntity;
import com.xavier.client_backend.mappers.Mapper;
import com.xavier.client_backend.services.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@CrossOrigin("https://mango-plant-0dc82e003.5.azurestaticapps.net")
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;
    private final Mapper<ProductEntity, ProductDto> productMapper;

    public ProductController(ProductService productService, Mapper<ProductEntity, ProductDto> productMapper) {
        this.productService = productService;
        this.productMapper = productMapper;
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> listProducts() {
        List<ProductEntity> products = productService.findAll();
        List<ProductDto> productDtos = products.stream()
                .map(productMapper::mapTo)
                .collect(Collectors.toList());
        return new ResponseEntity<>(productDtos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long id) {
        Optional<ProductEntity> product = productService.getProductById(id);
        if (product.isPresent()) {
            ProductDto productDto = productMapper.mapTo(product.get());
            return new ResponseEntity<>(productDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
