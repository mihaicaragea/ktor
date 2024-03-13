package ui.cart


import com.example.model.DataManagerMongoDb
import io.ktor.http.content.PartData
import io.ktor.server.application.call
import io.ktor.server.html.respondHtmlTemplate
import io.ktor.server.request.receiveMultipart
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.sessions.get
import io.ktor.server.sessions.sessions
import kotlinx.html.i
import org.slf4j.LoggerFactory
import ui.Endpoints
import ui.books.BookTemplate
import ui.login.Session

fun Route.cart(){
  get(Endpoints.CART.url){
    val session = call.sessions.get<Session>()
    call.respondHtmlTemplate(CartTemplate(session
      , DataManagerMongoDb.INSTANCE.cartForUser(session))){}
  }
  post(Endpoints.DOADDTOCART.url) {
    val log = LoggerFactory.getLogger("LoginView")
    val multipart = call.receiveMultipart()
    val session = call.sessions.get<Session>()
    var bookid: String =""
    while (true) {
      val part = multipart.readPart() ?: break
      when (part) {
        is PartData.FormItem -> {
          log.info("FormItem: ${part.name} = ${part.value}")
          if (part.name == "bookid")
            bookid = part.value
        } else -> {}
      }
      part.dispose()
    }
    val book = DataManagerMongoDb.INSTANCE.getBookWithId(bookid)
    DataManagerMongoDb.INSTANCE.addBook(session, book)
    call.respondHtmlTemplate(BookTemplate(call.sessions.get<Session>(), DataManagerMongoDb.INSTANCE.allBooks())){
      searchFilter{
        i{
          +"Book added to cart"
        }
      }
    }
  }

  post(Endpoints.DOREMOVEFROMCART.url){
    val log = LoggerFactory.getLogger("Remove from cart")
    val multipart = call.receiveMultipart()
    val session = call.sessions.get<Session>()
    var bookid: String =""
    while (true) {
      val part = multipart.readPart() ?: break
      when (part) {
        is PartData.FormItem -> {
          log.info("FormItem: ${part.name} = ${part.value}")
          if (part.name == "bookid")
            bookid = part.value
        } else -> {}
      }
      part.dispose()
    }
    val book = DataManagerMongoDb.INSTANCE.getBookWithId(bookid)
    DataManagerMongoDb.INSTANCE.removeBook(session, book)
    call.respondHtmlTemplate(CartTemplate(session, DataManagerMongoDb.INSTANCE.cartForUser(session))){
    }
  }
}