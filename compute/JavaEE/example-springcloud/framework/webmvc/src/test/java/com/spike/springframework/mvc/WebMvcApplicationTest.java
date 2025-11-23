package com.spike.springframework.mvc;

import com.spike.springframework.mvc.domain.model.Book;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import javax.sql.DataSource;
import java.math.BigDecimal;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT // 随机端口
) // Spring Boot集成测试
@ActiveProfiles("integration") // profile: integration
public class WebMvcApplicationTest {

    @Autowired
    private WebTestClient webTestClient; // WebFlux, 执行REST调用测试

    @Autowired
    private DataSource dataSource; // 验证自动装配的数据源

    @Test
    public void whenPostRequestThenBookCreated() {
        var expectedBook = Book.of("1231231231", "Title", "Author", BigDecimal.valueOf(9.90));
        webTestClient
                .post()
                .uri("/books")
                .bodyValue(expectedBook) // 请求体
                .exchange()// 发送请求
                // 预期响应状态
                .expectStatus().isCreated()
                // 预期响应体
                .expectBody(Book.class).value(actualBook -> {
                    Assertions.assertThat(actualBook).isNotNull();
                    Assertions.assertThat(actualBook.isbn())
                            .isEqualTo(expectedBook.isbn());
                });
    }
}
