package ui.books

import com.example.model.Book
import io.ktor.server.html.Placeholder
import io.ktor.server.html.Template
import io.ktor.server.html.insert
import kotlinx.html.ButtonType
import kotlinx.html.FlowContent
import kotlinx.html.FormEncType
import kotlinx.html.FormMethod
import kotlinx.html.HTML
import kotlinx.html.InputType
import kotlinx.html.ThScope
import kotlinx.html.button
import kotlinx.html.div
import kotlinx.html.form
import kotlinx.html.h2
import kotlinx.html.input
import kotlinx.html.table
import kotlinx.html.tbody
import kotlinx.html.td
import kotlinx.html.th
import kotlinx.html.thead
import kotlinx.html.tr
import ui.Endpoints
import ui.GeneralViewTemplate
import ui.login.Session

class BookTemplate(val  session: Session?, val books: List<Book>) : Template<HTML> {
  val basicTemplate: GeneralViewTemplate =  GeneralViewTemplate(session)
  val searchFilter = Placeholder<FlowContent>()
  override fun HTML.apply() {
    insert(basicTemplate) {
      content {
        div(classes = "mt-2") {
          h2() {
            + "Books available"
          }
          div {
            insert(searchFilter)
          }
          form(
            method = FormMethod.post,
            encType = FormEncType.multipartFormData,
            action = Endpoints.DOBOOKSEARCH.url
          ) {
            div(classes = "row mb03 mt-3") {
              div(classes = "md-6") {
                input(type = InputType.text, classes = "form-control", name = "search") {
                  this.placeholder = "Search for book"
                  this.attributes["arial-label"] = "Search"
                  this.attributes["arial-describedby"] = "basic-addon1"
                }
              }
              div(classes = "md-5 offset-md-1") {
                button(classes = "btn btn-primary", type = ButtonType.submit) {
                  + "Search"
                }
              }
            }
          }

          table(classes = "table table-striped") {
            thead {
              tr {
                th(scope = ThScope.col) { +"Id" }
                th(scope = ThScope.col) { +"Title" }
                th(scope = ThScope.col) { +"Author" }
                th(scope = ThScope.col) { +"Price" }
                th(scope = ThScope.col) { +"" }
              }
            }
            tbody {
              books.forEach() {
                tr {
                  td { +"${it.id}" }
                  td { +"${it.title}" }
                  td { +"${it.author}" }
                  td { +"${it.price}" }
                  td {
                    form(
                      method = FormMethod.post,
                      encType = FormEncType.multipartFormData,
                      action = Endpoints.DOADDTOCART.url
                    ) {
                      button(classes = "btn btn-success", type = ButtonType.submit) {
                        +"Add to cart"
                      }
                      input(type = InputType.hidden, name = "bookid") {
                        value="${it.id}"
                      }
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
  }
}