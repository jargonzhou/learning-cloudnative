package com.spike.spring.order.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ClientConfig {

    @Bean
    public WebClient webClient(ClientProperties clientProperties,
                               WebClient.Builder webClientBuilder) {
        return webClientBuilder
                .baseUrl(clientProperties.getCatalogServiceUri().toString())
                .build();
    }
}
