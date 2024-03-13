package ui

import io.ktor.server.html.PlaceholderList
import io.ktor.server.html.Template
import io.ktor.server.html.each
import io.ktor.server.html.insert
import kotlinx.html.ButtonType
import kotlinx.html.FlowContent
import kotlinx.html.UL
import kotlinx.html.a
import kotlinx.html.button
import kotlinx.html.div
import kotlinx.html.id
import kotlinx.html.li
import kotlinx.html.nav
import kotlinx.html.span
import kotlinx.html.ul
import ui.login.Session

class NavigationTemplate(val session: Session?) : Template<FlowContent> {
  val menuitems = PlaceholderList<UL,FlowContent>()
  override fun FlowContent.apply() {
    div {
      nav(classes = "navbar navbar-expand-md navbar-dark bg-dark") {
        a(classes = "navbar-brand", href = "#") { + "My Bookstore"}
        button(classes = "navbar-toggler", type = ButtonType.button) {
          this.attributes["data-toggle"] = "collapse"
          this.attributes["data-target"] = "#navbarsExampleDefault"
          this.attributes["aria-controls"] = "navbarsExampleDefault"
          this.attributes["aria-expanded"] = "false"
          this.attributes["aria-label"] = "Toggle navigation"
          span(classes = "navbar-toggler-icon") {}
        }
        div(classes = "collapse navbar-collapse") {
          this.id = "navbarsExampleDefault"
          ul(classes = "navbar-nav mr-auto") {
            each(menuitems) {
              li {
                insert(it)
              }
            }
          }
        }
      }
    }
  }
}