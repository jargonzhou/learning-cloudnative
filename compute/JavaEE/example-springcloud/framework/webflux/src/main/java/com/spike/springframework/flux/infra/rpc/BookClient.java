package com.spike.springframework.flux.infra.rpc;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

// 调用webmcv服务的客户端
@Component
public class BookClient {
    private static final String ROOT_API = "/books/";

    private final WebClient webClient;

    public BookClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<BookDto> getBookByIsbn(String isbn) {
        return webClient.get()
                .uri(ROOT_API + isbn)
                .retrieve() // 发送请求
                .bodyToMono(BookDto.class) // 处理响应
                // 超时时间: 带默认值
                .timeout(Duration.ofSeconds(3), Mono.empty())
                // 处理异常: 不存在
                .onErrorResume(WebClientResponseException.NotFound.class, exception -> Mono.empty())
                // 重试
                .retryWhen(
                        Retry.backoff(3, Duration.ofMillis(100))
                )
                // 其他异常
                .onErrorResume(Exception.class, exception -> Mono.empty());
    }
}
