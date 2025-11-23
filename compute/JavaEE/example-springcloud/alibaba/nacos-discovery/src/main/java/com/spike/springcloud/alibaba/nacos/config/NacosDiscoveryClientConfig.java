package com.spike.springcloud.alibaba.nacos.config;

import com.spike.springcloud.alibaba.nacos.api.NacosConfigClientApiFallback;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NacosDiscoveryClientConfig {
    @Bean
    public NacosConfigClientApiFallback nacosConfigClientApiFallback() {
        return new NacosConfigClientApiFallback();
    }
}
