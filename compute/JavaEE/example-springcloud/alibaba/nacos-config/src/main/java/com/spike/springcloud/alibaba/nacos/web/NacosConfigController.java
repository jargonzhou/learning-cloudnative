package com.spike.springcloud.alibaba.nacos.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

// 查看配置
@RestController
@RequestMapping("/config")
@RefreshScope // 配置自动更新
public class NacosConfigController {

    @Value("${userLocalCache:false}")
    private boolean userLocalCache;

    @Value("${common.userLocalCache:false}")
    private boolean commonUserLocalCache;

    @GetMapping("/get")
    public Map<String, Object> get() {
        return Map.of("userLocalCache", userLocalCache,
                "commonUserLocalCache", commonUserLocalCache);
    }
}
