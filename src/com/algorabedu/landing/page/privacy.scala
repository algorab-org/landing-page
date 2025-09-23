package com.algorabedu.landing.page

import com.algorabedu.landing.Translation
import scalatags.Text.all.*
import scalatags.Text.tags2.title as titleTag
import com.algorabedu.landing.tr
import com.algorabedu.landing.Email

def privacy(using lang: Translation) =
  html(
    head(
      titleTag("Algorab | " + tr"privacy.title"),
      meta(
        name := "viewport",
        content := "width=device-width, initial-scale=1.0",
        charset := "UTF-8"
      ),
      link(rel := "stylesheet", href := "/public/output.css"),
    ),
    body(
      navbar,
      div(
        cls := "bg-base-200 min-h-screen flex flex-col items-center gap-5 pt-20",
          h1(cls := "text-center text-5xl font-bold text-primary", tr"privacy.title"),
          p(cls := "text-center text-xl", tr"privacy.content"),
        )
      ),
      footerPart
    )