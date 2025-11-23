package com.spike.reactor.olddays.sse;

import com.spike.reactor.olddays.eventlistener.TemperatureSensor;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

// Access: http://localhost:18027/
@SpringBootTest
public class SSETest {

    @EnableAsync // 开启异步支持
    @SpringBootApplication
    public static class SSEApplication implements AsyncConfigurer {


        public static void main(String[] args) {
            SpringApplication.run(SSEApplication.class, args);
        }

        @Bean
        public TemperatureSensor sensor(ApplicationEventPublisher eventPublisher) {
            return new TemperatureSensor(eventPublisher);
        }

        //
        // 异步配置
        //

        @Override
        public Executor getAsyncExecutor() {
            return Executors.newFixedThreadPool(2);
        }

        @Override
        public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
            return new SimpleAsyncUncaughtExceptionHandler();
        }
    }
}
