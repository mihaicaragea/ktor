package ui.home

import io.ktor.server.html.Placeholder
import io.ktor.server.html.Template
import io.ktor.server.html.TemplatePlaceholder
import io.ktor.server.html.insert
import kotlinx.css.a
import kotlinx.css.menu
import kotlinx.html.FlowContent
import kotlinx.html.HTML
import kotlinx.html.a
import kotlinx.html.div
import kotlinx.html.h2
import kotlinx.html.p
import ui.Endpoints
import ui.GeneralViewTemplate
import ui.NavigationTemplate
import ui.login.Session

class LoginSuccessfulTemplate(val session: Session?) : Template<HTML> {
  val greeting = Placeholder<FlowContent>()
  val basicTemplate: GeneralViewTemplate = GeneralViewTemplate(session)
  val menu = TemplatePlaceholder<NavigationTemplate>()
  override fun HTML.apply() {
    insert(basicTemplate) {
      menu {
        menuitems {
          a(classes = "nav-link", href = Endpoints.HOME.url) {
            + "Home"
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
            + "You have been logged in!"
          }
          p{
            insert(greeting)
          }
        }
      }
    }
  }
}