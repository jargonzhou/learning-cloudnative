package com.spike.springframework.flux.infra.repository;

import com.spike.springframework.flux.domain.model.OrderStatus;
import com.spike.springframework.flux.domain.service.OrderService;
import com.spike.springframework.flux.infra.config.DataConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import reactor.test.StepVerifier;

import java.util.Objects;

@DataR2dbcTest // R2DBC测试
@Import(DataConfig.class)
@Testcontainers// 激活测试容器的启动和清理
public class OrderRepositoryTest {

    @Container // MySQL容器
    public static MySQLContainer<?> mysql = new MySQLContainer<>(DockerImageName.parse("mysql:8.0.36"));

    @Autowired
    private OrderRepository orderRepository;

    // 配置指向测试容器的属性
    @DynamicPropertySource
    public static void mysqlProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.r2dbc.url", () -> String.format("r2dbc:mysql://%s:%s/%s",
                mysql.getHost(),
                mysql.getMappedPort(MySQLContainer.MYSQL_PORT),
                mysql.getDatabaseName()));
        registry.add("spring.r2dbc.username", mysql::getUsername);
        registry.add("spring.r2dbc.password", mysql::getPassword);
        registry.add("spring.sql.init.mode", () -> "always"); // schema.sql
    }

    @Test
    public void createRejectedOrder() {
        var rejectedOrder = OrderService.buildRejectedOrder("1234567890", 3);

        // Reactor Test的验证器
        StepVerifier
                .create(orderRepository.save(rejectedOrder))
                .expectNextMatches(order -> {
                    System.err.println(order);
                    return order.status().equals(OrderStatus.REJECTED) && Objects.nonNull(order.id());
                })
                .verifyComplete();
    }
}
