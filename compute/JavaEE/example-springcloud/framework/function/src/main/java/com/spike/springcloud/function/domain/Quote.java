package com.spike.springcloud.function.domain;

public record Quote(
        String content,
        String author,
        Genre genre) {
}