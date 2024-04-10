package com.spike.spring.catalog;

import com.spike.spring.catalog.domain.Book;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.math.BigDecimal;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CatalogServiceApplicationTests {
    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void whenPostRequestThenBookCreated() {
        var book = Book.of("1231231231", "Title", "Author", new BigDecimal("9.90"));

        webTestClient
                .post()
                .uri("/books")
                .bodyValue(book)
                .exchange() // send the request
                .expectStatus().isCreated()
                .expectBody(Book.class).value(actualBook -> {
                    Assertions.assertThat(actualBook).isNotNull();
                    Assertions.assertThat(actualBook.isbn()).isEqualTo(book.isbn());
                });
    }

}
