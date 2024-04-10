package com.spike.spring.catalog;

import com.spike.spring.catalog.domain.Book;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;

public class BookValidationTests {
    private static Validator validator;

    @BeforeAll
    public static void setUpBeforeAll() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void whenAllFieldsCorrectThenValidationSucceeds() {
        var book = Book.of("1234567890", "Title", "Author", new BigDecimal("9.90"));

        Set<ConstraintViolation<Book>> violations = validator.validate(book);

        Assertions.assertThat(violations).isEmpty();
    }

    @Test
    public void whenIsbnDefinedButInvalidThenValidationFails() {
        var book = Book.of("a234567890", "Title", "Author", new BigDecimal("9.90"));
        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        Assertions.assertThat(violations).hasSize(1);
        Assertions.assertThat(violations.iterator().next().getMessage())
                .isEqualTo("The ISBN format must be valid.");
    }
}
