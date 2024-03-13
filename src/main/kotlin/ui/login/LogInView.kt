package ui.login

import com.example.Constants
import com.example.model.DataManagerMongoDb
import com.example.SecurityHandler
import io.ktor.http.content.PartData
import io.ktor.server.application.call
import io.ktor.server.html.respondHtmlTemplate
import io.ktor.server.request.receiveMultipart
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.sessions.get
import io.ktor.server.sessions.sessions
import org.slf4j.LoggerFactory
import ui.Endpoints
import ui.books.BookTemplate
import ui.home.HomeTemplate

data class Session(val username: String)

fun Route.loginView() {
  get(Endpoints.LOGIN.url) {
    call.respondHtmlTemplate(LoginTemplate(call.sessions.get<Session>())) {
    }
  }
  get(Endpoints.HOME.url) {
    call.respondHtmlTemplate(HomeTemplate(call.sessions.get<Session>())) {}
  }

  get(Endpoints.LOGOUT.url) {
    call.sessions.clear(Constants.COOKIE_NAME.value)
    call.respondHtmlTemplate(LogoutTemplate(call.sessions.get<Session>())) {}
  }

  post(Endpoints.DOLOGIN.url) {
    val log = LoggerFactory.getLogger("LoginView")
    val multipart = call.receiveMultipart()
    var username: String = ""
    var password: String = ""
    while (true) {
      val part = multipart.readPart() ?: break
      when (part) {
        is PartData.FormItem -> {
          log.info("FormItem: ${part.name} = ${part.value}")
          if (part.name == "username")
            username = part.value
          if (part.name == "password")
            password = part.value
        }

        is PartData.FileItem -> {
          log.info("FileItem: ${part.name} -> ${part.originalFileName} of ${part.contentType}")
        }

        else -> {
        }
      }
      part.dispose()
    }
    if (SecurityHandler().isValid(username, password)) {
      call.sessions.set(Constants.COOKIE_NAME.value, Session(username))
      call.respondHtmlTemplate(
        BookTemplate(
          call.sessions.get<Session>(),
          DataManagerMongoDb.INSTANCE.allBooks()
        )
      ) {
        searchFilter {
          +"You are logged in as $username and a cookie has been created"
        }
      }
    } else
      call.respondHtmlTemplate(LoginTemplate(call.sessions.get<Session>())) {
        greeting {
          +"Username or password was invalid... Try again."
        }
      }
  }
}