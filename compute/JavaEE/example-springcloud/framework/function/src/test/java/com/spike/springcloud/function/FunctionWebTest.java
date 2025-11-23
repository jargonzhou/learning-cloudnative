package com.spike.springcloud.function;

import com.spike.springcloud.function.domain.Genre;
import com.spike.springcloud.function.domain.Quote;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FunctionWebTest {
    private static final String PREFIX = "/func"; // 函数端点前缀

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void genreQuote() {
        webTestClient.post()
                .uri(PREFIX + "/genreQuote")
                .bodyValue(Genre.FANTASY.name())
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(Quote.class)
                .value(quote -> Assertions.assertThat(quote.genre()).isEqualTo(Genre.FANTASY));
    }

    @Test
    public void genreQuote_logQuote() {
        webTestClient.post()
                .uri(PREFIX + "/genreQuote,logQuote")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Genre.FANTASY.name())
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(Quote.class)
                .value(quote -> Assertions.assertThat(quote.genre()).isEqualTo(Genre.FANTASY));
    }
}
