package com.example.plugins

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*

fun Application.configureSecurity() {
  data class MySession(val count: Int = 0)
  install(Sessions) {
    cookie<MySession>("MY_SESSION") {
      cookie.extensions["SameSite"] = "lax"
    }
  }
  authentication {
    basic(name = "bookStoreAuth") {
      realm = "Book Store"
      validate { credentials ->
        if (credentials.name == "user" && credentials.password == "password") {
          UserIdPrincipal(credentials.name)
        } else {
          null
        }
      }
    }

    form(name = "myauth2") {
      userParamName = "user"
      passwordParamName = "password"
      challenge {
        /**/
      }
    }
  }
  routing {
    get("/session/increment") {
      val session = call.sessions.get<MySession>() ?: MySession()
      call.sessions.set(session.copy(count = session.count + 1))
      call.respondText("Counter is ${session.count}. Refresh to increment.")
    }
    authenticate("bookStoreAuth") {
      get("/protected/route/basic") {
        val principal = call.principal<UserIdPrincipal>()!!
        call.respondText("Hello ${principal.name}")
      }
    }
    authenticate("bookStoreAuth") {
      get("/protected/route/form") {
        val principal = call.principal<UserIdPrincipal>()!!
        call.respondText("Hello ${principal.name}")
      }
    }
  }
}
