package com.spike.spring.order.book;

import java.math.BigDecimal;

public record Book(
        String isbn,
        String title,
        String author,
        BigDecimal price
) {
}
