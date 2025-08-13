package com.polarbookshop.catalogservice.persistence;

import com.polarbookshop.catalogservice.domain.BookRepository;

public class BookAlreadyExistsException extends RuntimeException {
    public BookAlreadyExistsException(String isbn) {
        super("A book with ISBN " + isbn + " already exists.");
    }
}
