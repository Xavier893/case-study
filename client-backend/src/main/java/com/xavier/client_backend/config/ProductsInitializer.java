package com.xavier.client_backend.config;

import com.xavier.client_backend.domain.entities.ProductEntity;
import com.xavier.client_backend.repositories.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class ProductsInitializer {

    @Bean
    CommandLineRunner initProducts(ProductRepository productRepository) {
        return args -> {
            List<ProductEntity> products = Arrays.asList(
                    ProductEntity.builder().name("Controller").price(1199f).build(),
                    ProductEntity.builder().name("Monitor").price(499f).build(),
                    ProductEntity.builder().name("Keyboard").price(99f).build(),
                    ProductEntity.builder().name("Mouse").price(49f).build(),
                    ProductEntity.builder().name("Headset").price(149f).build(),
                    ProductEntity.builder().name("Laptop").price(1299f).build(),
                    ProductEntity.builder().name("Smartphone").price(999f).build(),
                    ProductEntity.builder().name("Tablet").price(599f).build(),
                    ProductEntity.builder().name("Smartwatch").price(199f).build(),
                    ProductEntity.builder().name("Camera").price(699f).build()
            );
            productRepository.saveAll(products);
        };
    }
}
