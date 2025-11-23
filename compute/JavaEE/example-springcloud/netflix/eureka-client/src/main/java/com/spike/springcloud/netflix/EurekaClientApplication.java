package com.spike.springcloud.netflix;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import com.spike.springcloud.netflix.feign.EurekaInstanceFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@SpringBootApplication
@EnableFeignClients(basePackages = "com.spike.springcloud.netflix.feign")
public class EurekaClientApplication {

    // use under the hood: RestClient, RestTemplate, WebClient, JerseyClient
    // alternative: Feign, Spring Cloud LoadBalancer
    @Autowired
    private EurekaClient eurekaClient;

    @Autowired
    private DiscoveryClient discoveryClient;

    // Feign客户端
    @Autowired
    private EurekaInstanceFeignClient eurekaInstanceFeignClient;

    @RequestMapping("/")
    public String home() {
        // 使用EurekaClient
        // 应用名称大写
        Application selfApplication = eurekaClient.getApplication("eureka-instance".toUpperCase());
        // 输出应用的实例
        List<InstanceInfo> eurekaInstance = selfApplication.getInstances();
        String instanceHomePages = String.join(",",
                Optional.ofNullable(eurekaInstance).orElse(List.of()).stream()
                        .map(InstanceInfo::getHomePageUrl)
                        .toList());

        // 使用DiscoveryClient
        List<ServiceInstance> instances = discoveryClient.getInstances("eureka-instance".toUpperCase());
        String instanceNames = String.join(",",
                Optional.ofNullable(instances).orElse(List.of()).stream()
                        .map(instance -> instance.getUri().toString())
                        .toList());

        return "EurekaClient:" + System.lineSeparator() +
                instanceHomePages + System.lineSeparator() +
                instanceNames;
    }

    @GetMapping("/feign")
    public Object feign() {
        return eurekaInstanceFeignClient.api();
    }

    @GetMapping("/feign/fallback")
    public Object feignFallback() {
        return eurekaInstanceFeignClient.apiNotFound();
    }

    public static void main(String[] args) {
        SpringApplication.run(EurekaClientApplication.class, args);
    }
}
