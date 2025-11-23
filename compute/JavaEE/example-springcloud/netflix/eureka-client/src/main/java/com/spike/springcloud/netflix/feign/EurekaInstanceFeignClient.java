package com.spike.springcloud.netflix.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

// override defaults: https://docs.spring.io/spring-cloud-openfeign/reference/spring-cloud-openfeign.html#spring-cloud-feign-overriding-defaults
@FeignClient(name = "${app.eureka-instance.name:eureka-instance}",
        fallback = EurekaInstanceFeignClientFallback.class)
public interface EurekaInstanceFeignClient {

    @GetMapping("/api")
    Object api();

    @GetMapping("/apinotfound")
    Object apiNotFound();
}
