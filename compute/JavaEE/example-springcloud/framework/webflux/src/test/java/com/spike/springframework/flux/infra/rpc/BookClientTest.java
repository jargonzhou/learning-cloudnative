package com.spike.springframework.flux.infra.rpc;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.IOException;

public class BookClientTest {

    private MockWebServer mockWebServer; // mock Web Server
    private BookClient bookClient;

    @BeforeEach
    public void setUp() throws IOException {
        this.mockWebServer = new MockWebServer();
        this.mockWebServer.start(); // 启动

        // 创建WebClient
        var webClient = WebClient.builder()
                .baseUrl(mockWebServer.url("/").uri().toString())
                .build();
        this.bookClient = new BookClient(webClient);
    }

    @AfterEach
    public void tearDown() throws IOException {
        this.mockWebServer.shutdown(); // 关闭
    }

    @Test
    void whenBookExistsThenReturnBook() {
        // mock响应
        var bookIsbn = "1234567890";
        var mockResponse = new MockResponse()
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody("""
                        {
                        "isbn": %s,
                        "title": "Title",
                        "author": "Author",
                        "price": 9.90,
                        "publisher": "Polarsophia"
                        }
                        """.formatted(bookIsbn));
        mockWebServer.enqueue(mockResponse);

        // 实际调用
        Mono<BookDto> book = bookClient.getBookByIsbn(bookIsbn);

        // Reactor Test的验证器
        StepVerifier.create(book)
                .expectNextMatches(
                        b -> b.isbn().equals(bookIsbn))
                .verifyComplete();
    }

}
