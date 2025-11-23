package com.spike.springcloud.netflix.feign;

import org.springframework.cloud.client.circuitbreaker.NoFallbackAvailableException;
import org.springframework.stereotype.Component;

// fallback实现
@Component
public class EurekaInstanceFeignClientFallback implements EurekaInstanceFeignClient {

    @Override
    public Object api() {
        throw new NoFallbackAvailableException("No fallback", new RuntimeException());
    }

    @Override
    public Object apiNotFound() {
        return "Fixed for not found";
    }
}