package com.spike.springcloud.gateway;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.session.data.redis.ReactiveRedisSessionRepository;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@AutoConfigureWebTestClient // 自动配置WebTestClient
public class GatewayServerTest {

    private static final int REDIS_PORT = 16379;
    @Container
    public static GenericContainer<?> redis = new GenericContainer<>(DockerImageName.parse("redis:7"))
            .withExposedPorts(REDIS_PORT);

    @DynamicPropertySource
    public static void redisProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.redis.host", () -> redis.getHost());
        registry.add("spring.redis,port", () -> redis.getMappedPort(REDIS_PORT));
    }

    @Autowired
    private WebTestClient webTestClient; // 测试用WebClient

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private ReactiveRedisSessionRepository reactiveRedisSessionRepository;

    @Test
    public void springContextLoads() {
        ReactiveRedisSessionRepository repository = applicationContext.getBean(ReactiveRedisSessionRepository.class);
        Assertions.assertThat(repository).isNotNull();
    }

    @Test
    public void sessionSaved() {
        // 模拟请求
        webTestClient
                .get()
                .uri("/books")
                .exchange() // 发送请求
                // 预期响应状态
                .expectStatus().is2xxSuccessful();

        ReactiveRedisOperations<String, Object> op = reactiveRedisSessionRepository.getSessionRedisOperations();
        op.keys("gateway-server:*")
                .subscribe(key -> op.opsForHash().entries(key)
                        .subscribe(entry -> System.err.println(key + "\t" + entry)));
    }

}
