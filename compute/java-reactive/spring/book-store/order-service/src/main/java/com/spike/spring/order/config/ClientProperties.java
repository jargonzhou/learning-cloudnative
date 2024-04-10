package com.spike.spring.order.config;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.net.URI;

@Data
@ConfigurationProperties(prefix = "book-store")
public class ClientProperties {
    @NotNull
    private URI catalogServiceUri;
}
