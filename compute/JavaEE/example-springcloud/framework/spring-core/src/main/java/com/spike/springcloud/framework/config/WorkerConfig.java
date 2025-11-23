package com.spike.springcloud.framework.config;

import com.spike.springcloud.framework.domain.woker.IWorker;
import com.spike.springcloud.framework.domain.woker.WorkerProperties;
import com.spike.springcloud.framework.domain.woker.WorkerRegistrar;
import com.spike.springcloud.framework.domain.woker.impl.HelloWorldWorker;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.Map;

@Configuration
// Import: @Configuration, ImportSelector, ImportBeanDefinitionRegistrar, or regular component classes to import.
@Import(WorkerRegistrar.class) // 会造成AOT处理失败, BUG???
// @ImportResource: XML
@EnableConfigurationProperties(WorkerProperties.class)
public class WorkerConfig {

    @ConditionalOnMissingBean(value = {IWorker.class}) // 默认Worker
    @Bean
    public Map<String, IWorker> workers() {
        HelloWorldWorker worker = new HelloWorldWorker();
        worker.setName("HelloWorld");
        worker.init(Map.of("greeting", "Spike"));
        return Map.of(worker.getName(), worker);
    }
}
