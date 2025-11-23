package com.spike.springframework.mvc.web;

import com.spike.springframework.mvc.domain.model.Book;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.math.BigDecimal;

@JsonTest // JSON序列化和反序列化测试
public class BookJsonTest {
    @Autowired
    private JacksonTester<Book> json; // 工具类

    // 序列化
    @Test
    public void testSerialize() throws Exception {
        var book = new Book(1L, "1234567890", "Title", "Author", BigDecimal.valueOf(9.90), 0);
        var jsonContent = json.write(book);
        Assertions.assertThat(jsonContent).extractingJsonPathStringValue("@.isbn")
                .isEqualTo(book.isbn());
        Assertions.assertThat(jsonContent).extractingJsonPathStringValue("@.title")
                .isEqualTo(book.title());
        Assertions.assertThat(jsonContent).extractingJsonPathStringValue("@.author")
                .isEqualTo(book.author());
        Assertions.assertThat(jsonContent).extractingJsonPathNumberValue("@.price")
                .isEqualTo(book.price().doubleValue());
    }

    // 反序列化
    @Test
    public void testDeserialize() throws Exception {
        var content = """
                {
                "id": 1,
                "isbn": "1234567890",
                "title": "Title",
                "author": "Author",
                "price": 9.90,
                "version": 0
                }
                """;
        Assertions.assertThat(json.parse(content))
                .usingRecursiveComparison()
                .isEqualTo(new Book(1L, "1234567890", "Title", "Author", new BigDecimal("9.90"), 0));
    }
}
