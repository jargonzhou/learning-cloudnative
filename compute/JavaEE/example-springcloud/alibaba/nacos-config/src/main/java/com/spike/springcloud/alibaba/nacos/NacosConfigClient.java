package com.spike.springcloud.alibaba.nacos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@EnableDiscoveryClient // 开启服务注册与发现
public class NacosConfigClient {
    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext =
                SpringApplication.run(NacosConfigClient.class, args);
        // 直接查看配置
        Boolean userLocalCache = applicationContext.getEnvironment().getProperty("userLocalCache", Boolean.class);
        Boolean commonUserLocalCache = applicationContext.getEnvironment().getProperty("common.userLocalCache", Boolean.class);
        System.err.println("userLocalCache: " + userLocalCache);
        System.err.println("commonUserLocalCache: " + commonUserLocalCache);
    }
}
