package ui.login

import io.ktor.server.html.Placeholder
import io.ktor.server.html.Template
import io.ktor.server.html.insert
import kotlinx.html.ButtonType
import kotlinx.html.FlowContent
import kotlinx.html.FormEncType
import kotlinx.html.FormMethod
import kotlinx.html.HTML
import kotlinx.html.InputType
import kotlinx.html.a
import kotlinx.html.button
import kotlinx.html.div
import kotlinx.html.form
import kotlinx.html.h2
import kotlinx.html.input
import kotlinx.html.p
import ui.Endpoints
import ui.GeneralViewTemplate

class LoginTemplate(val session: Session?) : Template<HTML> {
  val greeting = Placeholder<FlowContent>()
  private val basicTemplate: GeneralViewTemplate = GeneralViewTemplate(session)
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
          h2 {
            + "Welcome to the \"Bookstore\""
          }
          p{
            insert(greeting)
          }
        }
        form(
          method = FormMethod.post,
          encType = FormEncType.multipartFormData,
          action = Endpoints.DOLOGIN.url
        ) {
          div(classes = "mb-3") {
            input(type = InputType.text, classes = "form-control", name = "username") {
              this.placeholder = "Type in your username here..."
              this.attributes["aria-label"] = "Username"
              this.attributes["aria-describedby"] = "basic-addon1"
            }
          }
          div(classes = "mb-3") {
            input(type = InputType.text, classes = "form-control", name = "password") {
              this.placeholder = "Type in your username here..."
              this.attributes["aria-label"] = "Password"
              this.attributes["aria-describedby"] = "basic-addon1"
            }
          }
          div(classes = "mb-3") {
              button(classes = "btn btn-primary", type = ButtonType.submit) {
                + "Login"
              }
          }
        }
      }
    }
  }
}