package com.polarbookshop.catalogservice.domain;

import com.polarbookshop.catalogservice.config.DataConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.throwable;

import java.util.Optional;

@DataJdbcTest
@Import(DataConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("integration")
public class BookRepositoryJdbcTests {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private JdbcAggregateTemplate  jdbcAggregateTemplate;

    @Test
    void findBookByIsbnWhenExisting() {
        String bookIsbn = "0123456789";
        Book book = Book.of(bookIsbn, "NewTestBook", "Mr.Me", 10.76);
        jdbcAggregateTemplate.insert(book);
        Optional<Book> actualBook = bookRepository.findByIsbn(bookIsbn);
        assertThat(actualBook).isPresent();
        assertThat(actualBook.get().isbn()).isEqualTo(bookIsbn);
    }

    @Test
    void deleteBookAfterInserting() {
        String isbn = "1234117890";
        Book book = Book.of(isbn, "ToBeDeleted", "Mr.K", 3.76);
        Book savedBook = jdbcAggregateTemplate.insert(book);
        // verify it exists
        Optional<Book> actualBook = bookRepository.findByIsbn(isbn);
        assertThat(actualBook).isPresent();

        // now delete and make sure its gone
        bookRepository.delete(savedBook);
        Optional<Book> deletedBook = bookRepository.findByIsbn(isbn);
        assertThat(deletedBook).isNotPresent();
    }

    @Test
    void editBookAfterInserting() {
        String isbn = "1234567890";
        Book book = Book.of(isbn, "Edited", "Me", 8.21);
        Book savedBook = jdbcAggregateTemplate.insert(book);
        // verify it exists
        Optional<Book> actualBook = bookRepository.findByIsbn(isbn);
        assertThat(actualBook).isPresent();

        // now edit the book
        Book editedBook = new Book(
                savedBook.id(),
                savedBook.isbn(),
                "New Edited Book",
                "Me Still",
                3.10,
                savedBook.createdDate(),
                savedBook.lastModifiedDate(),
                savedBook.version()
        );
        assertThat(editedBook.isbn()).isEqualTo(book.isbn());
        bookRepository.findByIsbn(isbn).map(existingBook -> (bookRepository.save(editedBook)));
        Optional<Book> checkBook = bookRepository.findByIsbn(isbn);
        assertThat(checkBook).isPresent();
        assertThat(checkBook.get().title()).isEqualTo(editedBook.title());
        assertThat(checkBook.get().price()).isEqualTo(editedBook.price());
        assertThat(checkBook.get().author()).isEqualTo(editedBook.author());
        assertThat(checkBook.get().isbn()).isEqualTo(editedBook.isbn());
    }
}
