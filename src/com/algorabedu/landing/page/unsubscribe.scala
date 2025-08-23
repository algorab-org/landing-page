package com.algorabedu.landing.page

import com.algorabedu.landing.Translation
import scalatags.Text.all.*
import scalatags.Text.tags2.title as titleTag
import com.algorabedu.landing.tr
import com.algorabedu.landing.Email

def unsubscribe(email: String)(using lang: Translation) =
  html(
    head(
      titleTag("Algorab | Unsubscribed"),
      meta(charset := "UTF-8"),
      link(rel := "stylesheet", href := "/public/output.css"),
      script(src := "/public/js/index.js")
    ),
    body(
      div(
        id := "alert_container",
        cls := "fixed top-0 z-1 w-full flex flex-col items-center gap-3 m-w-xl pt-5"
      ),
      div(
        cls := "bg-base-200 min-h-screen flex flex-col justify-evenly items-center gap-10",
        div(
          cls := "flex flex-col items-center gap-5",
          h1(cls := "text-center text-5xl font-bold text-primary", tr"unsubscribe.title"),
          h2(cls := "text-center text-2xl", tr"unsubscribe.subtitle"),
        ),
        div(
          cls := "flex flex-col items-center gap-10",
          form(
            id := "subscription_form",
            onsubmit := "return onSubmit(this)",
            input(`type` := "hidden", name := "lang", value := lang.language),
            input(tpe := "hidden", name := "email", value := email),
            newsletterButton(tr"unsubscribe.resubscribe")(
              cls := "btn btn-xl btn-primary",
              tpe := "submit"
            )
          ),
          a(
            cls := "link text-md",
            href := s"/?lang=${lang.language}",
            tr"unsubscribe.home"
          )
        )
      )
    )
  )