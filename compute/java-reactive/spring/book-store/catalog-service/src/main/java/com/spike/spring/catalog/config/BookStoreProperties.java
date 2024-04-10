package com.spike.spring.catalog.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@Data
@RefreshScope // listen for RefreshScopeRefreshedEvent
@ConfigurationProperties(prefix="book-store")
public class BookStoreProperties {
    /**
     * A message to welcome users.
     */
    private String greeting;
}
