package com.spike.springcloud.alibaba.nacos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient // 开启服务注册与发现
@EnableFeignClients(basePackages = {"com.spike.springcloud.alibaba.nacos.api"})
public class NacosDiscoveryClientApplication {
    public static void main(String[] args) {
        SpringApplication.run(NacosDiscoveryClientApplication.class, args);
    }
}
