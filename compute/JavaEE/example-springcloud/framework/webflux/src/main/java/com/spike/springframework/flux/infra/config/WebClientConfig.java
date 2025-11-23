package com.spike.springframework.flux.infra.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Bean
    public WebClient webClient(ClientProperties clientProperties, // 属性配置
                               WebClient.Builder webClitBuilder // WebClient构建器: 自动配置的
    ) {
        return webClitBuilder
                .baseUrl(clientProperties.webmvcServiceUri().toString())
                .build();
    }
}
