package com.polarbookshop.catalogservice.web;

import com.polarbookshop.catalogservice.domain.BookService;
import com.polarbookshop.catalogservice.persistence.BookNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
public class BookControllerMvcTests {

    @Autowired
    private MockMvc mockMvc;

    // deprecated approach with MockBean
    // @MockBean
    // private BookService bookService;

    // Constructor based injection preferred
    @Configuration
    static class Config {
        @Bean
        public static BookService bookServiceMock() {
            return mock(BookService.class);
        }
    }

    BookService bookService = Config.bookServiceMock();

    @Test
    void whenGetBookNotExistingThenShouldReturn404() throws Exception {
        String isbn = "1247261940";
        given(bookService.viewBookDetails(isbn)).willThrow(BookNotFoundException.class);
        mockMvc.perform(get("/books/" + isbn)).andExpect(status().isNotFound());
    }

}
