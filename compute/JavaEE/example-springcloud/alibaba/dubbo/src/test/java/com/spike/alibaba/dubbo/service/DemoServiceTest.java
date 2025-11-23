package com.spike.alibaba.dubbo.service;

import org.apache.dubbo.config.bootstrap.builders.ReferenceBuilder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class DemoServiceTest {
    @Test
    public void invokeService() {
        DemoService demoService = (DemoService) ReferenceBuilder.newBuilder()
                .interfaceClass(DemoService.class)
                .id("dubbo-demo-app-consumer")
                .url("tri://localhost:50051")
                .build()
                .get();

        String message = demoService.sayHello("dubbo");
        System.err.println(message);
        Assertions.assertThat(message).contains("dubbo");
    }
}
