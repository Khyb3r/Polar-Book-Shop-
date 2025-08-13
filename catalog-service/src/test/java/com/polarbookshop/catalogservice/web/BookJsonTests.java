package com.polarbookshop.catalogservice.web;

import com.polarbookshop.catalogservice.domain.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import static org.assertj.core.api.Assertions.assertThat;
import java.io.IOException;
import java.time.Instant;

@JsonTest
public class BookJsonTests {
    @Autowired
    private JacksonTester<Book> json;

    @Test
    void serialiseTest() throws IOException {
        Instant now = Instant.now();
        var book = new Book(210L, "1234567890", "Title", "Author", 21.2, now, now,2);
        JsonContent<Book> jsonContent = json.write(book);
        assertThat(jsonContent).extractingJsonPathNumberValue("@.id")
                .isEqualTo(book.id().intValue());
        assertThat(jsonContent).extractingJsonPathStringValue("@.isbn")
                .isEqualTo(book.isbn());
        assertThat(jsonContent).extractingJsonPathStringValue("@.author")
                .isEqualTo(book.author());
        assertThat(jsonContent).extractingJsonPathNumberValue("@.price")
                .isEqualTo(book.price());
        assertThat(jsonContent).extractingJsonPathStringValue("@.title")
                .isEqualTo(book.title());
        assertThat(jsonContent).extractingJsonPathValue("@.createdDate")
                .isEqualTo(book.createdDate().toString());
        assertThat(jsonContent).extractingJsonPathValue("@.lastModifiedDate")
                .isEqualTo(book.lastModifiedDate().toString());
        assertThat(jsonContent).extractingJsonPathNumberValue("@.version")
                .isEqualTo(book.version());
    }

    @Test
    void deserializeTest() throws IOException {
        Instant instant = Instant.parse("2025-08-10T10:34:56Z");
        String jsonFormat = "{\n" +
                " \"id\": 200,\n" +
                " \"isbn\": \"1234567890\",\n" +
                " \"title\": \"Title\",\n" +
                " \"author\": \"Author\",\n" +
                " \"price\": 9.90,\n" +
                " \"createdDate\": \"2025-08-10T10:34:56Z\",\n" +
                " \"lastModifiedDate\": \"2025-08-10T10:34:56Z\",\n" +
                " \"version\": 3\n" +
                "}";
        assertThat(json.parse(jsonFormat)).usingRecursiveComparison().
                isEqualTo(new Book(200L, "1234567890", "Title",
                        "Author", 9.90, instant, instant,3));
    }
}
