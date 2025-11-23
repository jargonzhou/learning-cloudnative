package com.spike.springcloud.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// example from 'Cloud Native Spring in Action': edge-service
// ops: https://github.com/jargonzhou/application-store/tree/main/data-engineering/redis/single
@SpringBootApplication
public class GatewayServer {
    public static void main(String[] args) {
        SpringApplication.run(GatewayServer.class, args);
    }
}
