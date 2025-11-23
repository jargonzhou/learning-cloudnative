package com.spike.springcloud.alibaba.nacos.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@FeignClient(name = "nacos-client", contextId = "nacos-client", fallback = NacosConfigClientApiFallback.class)
public interface NacosConfigClientApi {
    @GetMapping("/config/get")
    Map<String, Object> get();
}
