package com.spike.springframework.mvc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// example from 'Cloud Native Spring in Action': catalog-service
// ops: https://github.com/jargonzhou/application-store/tree/main/data-engineering/mysql/8
@SpringBootApplication
public class WebMvcApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebMvcApplication.class, args);
    }
}
