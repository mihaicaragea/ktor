package ui.login

import io.ktor.server.html.Placeholder
import io.ktor.server.html.Template
import io.ktor.server.html.insert
import kotlinx.html.FlowContent
import kotlinx.html.HTML
import kotlinx.html.a
import kotlinx.html.div
import kotlinx.html.h2
import ui.Endpoints
import ui.GeneralViewTemplate

class LogoutTemplate(val basicTemplate: GeneralViewTemplate = GeneralViewTemplate()) : Template<HTML> {
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
      }
      content {
        div(classes = "mt-2") {
          h2() {
            + "You have been logged out!"
          }
        }
      }
    }
  }
}