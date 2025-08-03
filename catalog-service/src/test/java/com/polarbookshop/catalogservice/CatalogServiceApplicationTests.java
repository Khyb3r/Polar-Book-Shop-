package com.polarbookshop.catalogservice;

import com.polarbookshop.catalogservice.domain.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
	webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
class CatalogServiceApplicationTests {

	@Autowired
	private WebTestClient webTestClient;

	@Test
	void whenPostRequestThenBookCreated() {
		var expectedBook = new Book("1234567890", "Title", "Author", 9.90);
		webTestClient.post().uri("/books").bodyValue(expectedBook).exchange().expectStatus().isCreated().
				expectBody(Book.class).value(actualBook -> {
					assertThat(actualBook).isNotNull();
					assertThat(actualBook.isbn()).isEqualTo(expectedBook.isbn());
				});
	}

	@Test
	void whenPostRequestCreateBookAndWhenDeleteRequestDestroyBook() {
		String isbn = "0987654321";
		var book = new Book(isbn, "AboutToDelete", "DeletedAuthor", 10.90);
		webTestClient.post().uri("/books").bodyValue(book).exchange().expectStatus().isCreated().
				expectBody(Book.class).value(actualBook -> {
					assertThat(actualBook).isNotNull();
					assertThat(actualBook.isbn()).isEqualTo(book.isbn());
				});
		// delete
		webTestClient.delete().uri("/books/{isbn}", isbn).exchange().expectStatus().isNoContent();
	}

	@Test
	void whenPutRequestBookEdited() {
		String isbn = "1456172901";
		var book = new Book(isbn, "EditBook", "EditedAuthor", 4.40);
		// create book first then edit
		webTestClient.post().uri("/books").bodyValue(book).exchange().expectStatus().isCreated()
						.expectBody(Book.class).value(actualBook -> {
							assertThat(actualBook).isNotNull();
							assertThat(actualBook.isbn()).isEqualTo(book.isbn());
				});

		// put
		var editedBook = new Book(isbn, "EditedBookNow", "NewAuthor", 100.10);
		webTestClient.put().uri("/books/{isbn}", isbn).bodyValue(editedBook).exchange()
				.expectBody(Book.class).value(newBook -> {
					assertThat(editedBook).isNotNull();
					assertThat(newBook.isbn()).isEqualTo(editedBook.isbn());
					assertThat(newBook.author()).isEqualTo(editedBook.author());
					assertThat(newBook.price()).isEqualTo(editedBook.price());
					assertThat(newBook.title()).isEqualTo(editedBook.title());
				});
	}

	@Test
	void contextLoads() {
	}



}
