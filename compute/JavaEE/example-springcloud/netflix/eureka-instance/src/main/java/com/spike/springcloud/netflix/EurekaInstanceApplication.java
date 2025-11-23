package com.spike.springcloud.netflix;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.cloud.commons.util.InetUtilsProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@SpringBootApplication
public class EurekaInstanceApplication {

    @GetMapping("/api")
    public Object api() {
        try (InetUtils inetUtils = new InetUtils(new InetUtilsProperties())) {
            return Map.of("service", "eureka-instance",
                    "timestamp", System.currentTimeMillis(),
                    "host", inetUtils.findFirstNonLoopbackHostInfo());
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(EurekaInstanceApplication.class, args);
    }
}
