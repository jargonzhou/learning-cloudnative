package com.spike.springframework.flux.infra.rpc;

import java.math.BigDecimal;

// DTO
public record BookDto(
        String isbn,
        String title,
        String author,
        BigDecimal price
) {
}