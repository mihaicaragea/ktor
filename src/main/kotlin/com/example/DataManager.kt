package com.example

import Book

class DataManager {

  var books = ArrayList<Book>()

  fun giveMeId(): String { return books.size.toString()}

  init {
    books.add(Book(giveMeId(), "How to grow apples", "Mr. Appleton", 100.00f))
    books.add(Book(giveMeId(), "How to grow oranges", "Mr. Orangeton", 90.00f))
    books.add(Book(giveMeId(), "How to grow lemons", "Mr. Lemon", 110.00f))
    books.add(Book(giveMeId(), "How to grow pineapples", "Mr. Pineapple", 100.00f))
    books.add(Book(giveMeId(), "How to grow pears", "Mr. Pears", 110.00f))
    books.add(Book(giveMeId(), "How to grow coconuts", "Mr. Coconut", 130.00f))
    books.add(Book(giveMeId(), "How to grow bannanas", "Mr. Appleton", 120.00f))
  }

  fun newBook(book: Book) : Book {
    books.add(book)
    return book
  }

  fun updateBook(book: Book): Book? {
    val foundBook = books.find { it.id == book.id }

    foundBook?.title = book.title
    foundBook?.author = book.author
    foundBook?.price = book.price

    return foundBook
  }

  fun deleteBook(book: Book): Book? {
    val foundBook = books.find { it.id == book.id }

    books.remove(foundBook)

    return foundBook
  }

  fun deleteBook(bookId: String): Book {
    val foundBook = books.find { it.id == bookId }

    books.remove(foundBook)

    return foundBook!!
  }

  fun allBooks(): List<Book> {
    return books
  }
}