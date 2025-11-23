package com.spike.springcloud.alibaba.sentinel.domain;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import org.springframework.stereotype.Service;

@Service
public class ExampleService {

    @SentinelResource(value = "sayHello") // 标识资源: 服务层
    public String sayHello(String name) {
        return "Hello, " + name;
    }

    // non resource
    public String sayWorld(String world) {
        return "World: " + world;
    }
}
