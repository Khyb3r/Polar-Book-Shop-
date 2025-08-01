package com.polarbookshop.catalogservice.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public record Book (
  @NotBlank(message = "Book ISBN must be defined.")
  @Pattern(
    regexp = "^([0-9]{10} | [0-9] {13})$",
    message = "ISBN format must be valid.")
  String isbn,

  @NotBlank(message = "Book Title must be defined.")
  String title,

  @NotBlank(message = "Book Author must be defined.")
  String author,

  @NotBlank(message = "Book Price must be defined.")
  @Positive(message = "Price must be greater than Zero.")
  Double price
) {}
