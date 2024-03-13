package com.example

import io.ktor.http.content.PartData
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.html.respondHtmlTemplate
import io.ktor.server.locations.Location
import io.ktor.server.locations.get
import io.ktor.server.request.receive
import io.ktor.server.request.receiveMultipart
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import io.ktor.server.sessions.get
import io.ktor.server.sessions.sessions
import kotlinx.css.i
import kotlinx.html.i
import org.slf4j.LoggerFactory
import ui.Endpoints
import ui.Endpoints.DOBOOKSEARCH
import ui.books.BookTemplate
import ui.login.Session

@Location("/book/lists")
data class BookListLocations(val sortBy: String, val asc: Boolean)

fun Route.books() {
  val dataManager =  DataManagerMongoDb.INSTANCE

  authenticate("bookStoreAuth") {
    get<BookListLocations>() {
      call.respond(dataManager.sortedBooks(it.sortBy, it.asc))
    }
  }

  route("/book") {
    get("/") {
      call.respond(dataManager.allBooks())
    }

    post("/{id}") {
      val book = call.receive(Book::class)
      val updatedBook = dataManager.updateBook(book)
      call.respondText { "The book has been updated $updatedBook" }
    }

    put("/") {
      val book = call.receive(Book::class)
      val newBook = dataManager.newBook(book)
      call.respondText { "The book has been created $newBook" }
    }

    delete("/{id}") {
      val id = call.parameters.get("id").toString()
      val deletedBook = dataManager.deleteBook(id)

      call.respond(deletedBook!!)
    }
  }

    get(Endpoints.BOOKS.url) {
      call.respondHtmlTemplate(
        BookTemplate(
          call.sessions.get<Session>(),
          DataManagerMongoDb.INSTANCE.allBooks()
        )
      ) {
      }
    }

    post(DOBOOKSEARCH.url){
      val log = LoggerFactory.getLogger("LoginView")
      val multipart = call.receiveMultipart()
      var search: String =""
      while (true) {
        val part = multipart.readPart() ?: break
        when (part) {
          is PartData.FormItem -> {
            log.info("FormItem: ${part.name} = ${part.value}")
            if (part.name == "search")
              search = part.value
          } else -> {}
        }
        part.dispose()
      }
      val searchBooks = DataManagerMongoDb.INSTANCE.searchBooks(search)
      call.respondHtmlTemplate(BookTemplate(call.sessions.get<Session>(), searchBooks)){
        searchFilter{
          i{
            +"Search filter: $search"
          }
        }
      }
    }
  }