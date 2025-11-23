package com.spike.springframework.mvc.infra.repository;

import com.spike.springframework.mvc.domain.model.Book;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Optional;

@DataJdbcTest // Spring Data JDBC测试
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.NONE // 使用Testcontainers, 不用替换测试数据源
)
@ActiveProfiles("integration") // profile: integration
public class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private JdbcAggregateTemplate jdbcAggregateTemplate; // 与数据库交互的底层对象

    @Test
    public void findBookByIsbnWhenExisting() {
        var bookIsbn = "1234561237";
        var book = Book.of(bookIsbn, "Title", "Author", BigDecimal.valueOf(12.90));
        // 插入数据
        var bookSaved = jdbcAggregateTemplate.insert(book);

        // 使用仓库查询
        Optional<Book> actualBook = bookRepository.findByIsbn(bookIsbn);
        Assertions.assertThat(actualBook).isPresent();
        Assertions.assertThat(actualBook.get().isbn()).isEqualTo(book.isbn());

        // 清理
        jdbcAggregateTemplate.deleteById(bookSaved.id(), Book.class);
    }
}
