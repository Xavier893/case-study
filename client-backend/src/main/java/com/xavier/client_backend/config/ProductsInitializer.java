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
            if (productRepository.count() == 0) {
                List<ProductEntity> products = Arrays.asList(
                        ProductEntity.builder().name("Smartphone").price(699.99f).build(),
                        ProductEntity.builder().name("Laptop").price(999.99f).build(),
                        ProductEntity.builder().name("Tablet").price(399.99f).build(),
                        ProductEntity.builder().name("Smartwatch").price(199f).build(),
                        ProductEntity.builder().name("Wireless Earbuds").price(149.99f).build(),
                        ProductEntity.builder().name("Gaming Console").price(499.99f).build(),
                        ProductEntity.builder().name("4K TV").price(799.99f).build(),
                        ProductEntity.builder().name("Bluetooth Speaker").price(99.99f).build(),
                        ProductEntity.builder().name("External Hard Drive").price(89.99f).build(),
                        ProductEntity.builder().name("Gaming Mouse").price(59.99f).build(),
                        ProductEntity.builder().name("Mechanical Keyboard").price(129.99f).build(),
                        ProductEntity.builder().name("Drone").price(299.99f).build(),
                        ProductEntity.builder().name("VR Headset").price(349.99f).build(),
                        ProductEntity.builder().name("Smart Home Hub").price(179.99f).build(),
                        ProductEntity.builder().name("Fitness Tracker").price(79.99f).build()
                );
                productRepository.saveAll(products);
            }
        };
    }
}
