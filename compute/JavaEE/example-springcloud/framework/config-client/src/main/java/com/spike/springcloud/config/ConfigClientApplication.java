package com.spike.springcloud.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RefreshScope // 刷新配置: RefreshScopeRefreshedEvent
@RestController
@SpringBootApplication
public class ConfigClientApplication {
    @Value("${info.name:''}") // 使用@Value
    private String name;

    @Value("${info.description:''}")
    private String description;

    @Autowired
    private Environment environment; // 从环境中获取

    @Autowired
    private ConfigClientProperties configClientProperties; // 使用ConfigurationProperties

    @GetMapping("/config")
    public Object config() {
        String greeting = environment.getProperty("greeting", "");

        return Map.of("name", name,
                "description", description,
                "greeting", greeting,
                "app", Map.of("name", configClientProperties.getName(),
                        "description", configClientProperties.getDescription()));
    }


    public static void main(String[] args) {
        SpringApplication.run(ConfigClientApplication.class, args);
    }
}
