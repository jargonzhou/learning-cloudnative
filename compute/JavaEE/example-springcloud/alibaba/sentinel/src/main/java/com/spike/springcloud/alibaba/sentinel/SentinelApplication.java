package com.spike.springcloud.alibaba.sentinel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// example: https://github.com/alibaba/spring-cloud-alibaba/wiki/Sentinel
// intergration: https://sentinelguard.io/zh-cn/docs/open-source-framework-integrations.html
@SpringBootApplication
public class SentinelApplication {
    public static void main(String[] args) {
        SpringApplication.run(SentinelApplication.class, args);
    }
}
