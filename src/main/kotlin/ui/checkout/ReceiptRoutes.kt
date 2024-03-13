package ui.checkout

import com.example.model.DataManagerMongoDb
import io.ktor.server.application.call
import io.ktor.server.html.respondHtmlTemplate
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.sessions.get
import io.ktor.server.sessions.sessions
import ui.Endpoints
import ui.login.Session

fun Route.receipt(){
  get(Endpoints.RECEIPT.url){
    val session = call.sessions.get<Session>()
    call.respondHtmlTemplate(ReceiptTemplate(session, DataManagerMongoDb.INSTANCE.cartForUser(session))){
    }
  }
}