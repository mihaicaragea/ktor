package com.example

import Book
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.locations.Location
import io.ktor.server.locations.get
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route

@Location("/book/lists")
data class BookListLocations(val sortBy: String, val asc: Boolean)

fun Route.books() {
  val dataManager = DataManager()

  authenticate("bookStoreAuth") {
    get<BookListLocations>() {
      call.respond(dataManager.sortedBooks(it.sortBy, it.asc))
    }
  }

  route("/book") {
    get ("/") {
      call.respond(dataManager.allBooks())
    }

    post("/{id}") {
      val book = call.receive(Book::class)
      val updatedBook = dataManager.updateBook(book)
      call.respondText {"The book has been updated $updatedBook"}
    }

    put("/") {
      val book = call.receive(Book::class)
      val newBook = dataManager.newBook(book)
      call.respondText { "The book has been created $newBook" }
    }

    delete("/{id}") {
      val id = call.parameters.get("id").toString()
      val deletedBook = dataManager.deleteBook(id)

      call.respond(deletedBook)
    }
  }
}