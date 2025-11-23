package com.spike.springframework.flux.infra.config;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.net.URI;

@ConfigurationProperties(prefix = "app")
public record ClientProperties(
        @NotNull
        URI webmvcServiceUri) {
}
