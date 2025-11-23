package com.spike.springcloud.stream;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// example from 'Cloud Native Spring in Action': order-service
// ops: https://github.com/jargonzhou/application-store/tree/main/data-engineering/rabbitmq
@SpringBootApplication
public class StreamConsumerApplication {
    public static void main(String[] args) {
        SpringApplication.run(StreamConsumerApplication.class, args);
    }
}
