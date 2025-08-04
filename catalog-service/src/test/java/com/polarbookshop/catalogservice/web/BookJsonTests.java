package com.polarbookshop.catalogservice.web;

import com.polarbookshop.catalogservice.domain.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import static org.assertj.core.api.Assertions.assertThat;
import java.io.IOException;

@JsonTest
public class BookJsonTests {
    @Autowired
    private JacksonTester<Book> json;

    @Test
    void serialiseTest() throws IOException {
        var book = new Book("1234567890", "Title", "Author", 21.2);
        JsonContent<Book> jsonContent = json.write(book);
        assertThat(jsonContent).extractingJsonPathStringValue("@.isbn")
                .isEqualTo(book.isbn());
        assertThat(jsonContent).extractingJsonPathStringValue("@.author")
                .isEqualTo(book.author());
        assertThat(jsonContent).extractingJsonPathNumberValue("@.price")
                .isEqualTo(book.price());
        assertThat(jsonContent).extractingJsonPathStringValue("@.title")
                .isEqualTo(book.title());
    }

    @Test
    void deserializeTest() throws IOException {
        String jsonFormat = "{\n" +
                " \"isbn\": \"1234567890\",\n" +
                " \"title\": \"Title\",\n" +
                " \"author\": \"Author\",\n" +
                " \"price\": 9.90\n" +
                "}";
        assertThat(json.parse(jsonFormat)).usingRecursiveComparison().
                isEqualTo(new Book("1234567890", "Title",
                        "Author", 9.90));
    }
}
