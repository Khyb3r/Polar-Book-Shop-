package com.polarbookshop.catalogservice.domain;

import jakarta.validation.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import java.util.Set;

public class BookValidationTests {
    private static Validator validator;

    // setup to use on Java Validation API
    @BeforeAll
    static void setUpAll() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Test
    void allFieldsCorrectShouldSucceed() {
        var book = Book.of("1234567890", "TestTitle", "AuthorH", 10.90);
        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertThat(violations).isEmpty();
    }

    @Test
    void IsbnDefinedButIncorrectShouldFail() {
        var book = Book.of("1234567890101001010102910", "TesterTitle", "AuthorF", 10.90);
        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo(
                "ISBN format must be valid.");
    }

    @Test
    void priceIsDefinedButNegativeShouldFail() {
        var book = Book.of("1234567890", "TestTitle", "AuthorK", -10.21);
        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo(
                "Price must be greater than Zero."
        );
    }

}
