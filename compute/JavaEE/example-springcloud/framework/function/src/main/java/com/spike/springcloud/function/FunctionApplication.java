package com.spike.springcloud.function;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// example from 'Cloud Native Spring in Action': dispatcher-service
// example from 'Cloud Native Spring in Action': quote-function
@SpringBootApplication
public class FunctionApplication {
    public static void main(String[] args) {
        SpringApplication.run(FunctionApplication.class, args);
    }
}
