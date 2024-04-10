package com.spike.spring.catalog.web;

import com.spike.spring.catalog.config.BookStoreProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    private final BookStoreProperties bookStoreProperties;

    public HomeController(BookStoreProperties bookStoreProperties) {
        this.bookStoreProperties = bookStoreProperties;
    }

    @GetMapping("/")
    public String getGreeting() {
        return bookStoreProperties.getGreeting();
    }
}
