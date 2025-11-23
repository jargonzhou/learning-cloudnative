package com.spike.security;

import com.spike.security.repository.ProductRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.List;
import java.util.Optional;

@SpringBootApplication
public class SecurityApplication {


    public static void main(String[] args) {
        ConfigurableApplicationContext context =
                SpringApplication.run(SecurityApplication.class, args);

        ProductRepository productRepository = context.getBean(ProductRepository.class);
        Optional.ofNullable(productRepository.findAll()).orElse(List.of()).forEach(p -> {
            System.err.println(p);
        });
    }
}
