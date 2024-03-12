package ui.home

import io.ktor.server.html.Placeholder
import io.ktor.server.html.Template
import io.ktor.server.html.insert
import kotlinx.html.FlowContent
import kotlinx.html.HTML
import kotlinx.html.a
import kotlinx.html.div
import kotlinx.html.h2
import kotlinx.html.p
import ui.Endpoints
import ui.GeneralViewTemplate

class HomeTemplate(val basicTemplate: GeneralViewTemplate = GeneralViewTemplate()) : Template<HTML> {
  val greeting = Placeholder<FlowContent>()
  override fun HTML.apply() {
    insert(basicTemplate) {
      menu {
        menuitems {
          a(classes = "nav-link", href = Endpoints.HOME.url) {
            + "Home"
          }
        }
        menuitems {
          a(classes = "nav-link", href = Endpoints.LOGIN.url) {
            + "Login"
          }
        }
        menuitems {
          a(classes = "nav-link", href = Endpoints.LOGOUT.url) {
            + "Logout"
          }
        }
      }
      content {
        div(classes = "mt-2") {
          h2() {
            + "Welcome to the Bookstore"
          }
          p{
            + " - We have many good deals on a lot of different topics"
          }
          p{
            + "Let us know if you are looking for something that we do not currently have."
          }
        }
      }
    }
  }
}