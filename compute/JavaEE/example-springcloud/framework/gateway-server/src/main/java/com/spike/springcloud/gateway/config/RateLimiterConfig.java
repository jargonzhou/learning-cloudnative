package com.spike.springcloud.gateway.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Configuration
public class RateLimiterConfig {
    // RequestRateLimiterGatewayFilterFactory所需的键解析器, 自动配置中默认为PrincipalNameKeyResolver
    @Bean
    public KeyResolver keyResolver() {
        return new KeyResolver() {
            @Override
            public Mono<String> resolve(ServerWebExchange exchange) {
                return Mono.just("any");
            }
        };
    }
}
