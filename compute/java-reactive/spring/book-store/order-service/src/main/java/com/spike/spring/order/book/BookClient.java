package com.spike.spring.order.book;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

@Component
public class BookClient {
    private static final String BOOKS_ROOT_API = "/books/";
    private final WebClient webClient;

    public BookClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<Book> getBookByIsbn(String isbn) {
        return webClient
                .get()
                .uri(BOOKS_ROOT_API + isbn)
                .retrieve()
                .bodyToMono(Book.class)
                // timeout, fallback
                .timeout(Duration.ofSeconds(3), Mono.empty())
                // exception handle
                .onErrorResume(WebClientResponseException.NotFound.class,
                        e -> Mono.empty())
                // retry
                .retryWhen(Retry.backoff(3, Duration.ofMillis(100)))
                // exception handle after retry
                .onErrorResume(Exception.class,
                        e -> Mono.empty())
                ;
    }
}
