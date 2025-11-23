package com.spike.springframework.flux;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

// example from 'Cloud Native Spring in Action': order-service
// ops: https://github.com/jargonzhou/application-store/tree/main/data-engineering/mysql/8
@SpringBootApplication
@ConfigurationPropertiesScan
public class WebFluxApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebFluxApplication.class, args);
    }
}
