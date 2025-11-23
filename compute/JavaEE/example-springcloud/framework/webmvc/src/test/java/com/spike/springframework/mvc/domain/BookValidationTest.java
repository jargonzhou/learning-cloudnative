package com.spike.springframework.mvc.domain;

import com.spike.springframework.mvc.domain.model.Book;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;

// 验证测试
public class BookValidationTest {
    private static Validator validator;

    @BeforeAll
    public static void setUpBeforeAll() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory();) {
            validator = factory.getValidator();
        }
    }

    @Test
    void whenAllFieldsCorrectThenValidationSucceeds() {
        var book = new Book(1L, "1234567890", "Title", "Author", BigDecimal.valueOf(9.90), 0);
        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        // AssertJ
        Assertions.assertThat(violations).isEmpty();
    }

    @Test
    void whenIsbnDefinedButIncorrectThenValidationFails() {
        var book = new Book(1L, "a234567890", "Title", "Author", BigDecimal.valueOf(9.90), 0);
        Set<ConstraintViolation<Book>> violations = validator.validate(book); // isbn校验失败
        Assertions.assertThat(violations).hasSize(1);
        Assertions.assertThat(violations.iterator()
                .next().getMessage()).isEqualTo("The ISBN format must be valid.");
    }
}
