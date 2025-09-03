package com.algorabedu.landing.mail

import com.algorabedu.landing.Translation
import scalatags.Text.all.{tr as trow, *}
import com.algorabedu.landing.tr

def centeredRow(modifiers: Modifier*) =
  trow(
    td(
      style := "text-align: center; padding-left: 40px; padding-right: 40px;",
      attr("align") := "center",
      modifiers
    )
  )

def subscribed(email: String, domain: String)(using lang: Translation) =
  html(
    body(
      style := """font-family: ui-sans-serif, system-ui, sans-serif, "Apple Color Emoji", "Segoe UI Emoji", "Segoe UI Symbol", "Noto Color Emoji";""",
      table(
        width := "100%",
        height := "100%",
        border := "0",
        attr("cellspacing") := "0",
        attr("cellpadding") := "0",
        style := "border-collapse: collapse;",
        tbody(
          centeredRow(
            h1(
              style := "color: oklch(65% 0.241 354.308);",
              tr"newsletter.subscribe.title"
            )
          ),
          centeredRow(
            p(tr"newsletter.subscribe.description")
          ),
          centeredRow(
            style := "height: auto;",
            a(
              display := "inline-block",
              style :=
                """background-color: oklch(65% 0.241 354.308);
                  |color: white;
                  |padding: 16px 32px;
                  |font-size: 24px;
                  |text-decoration: none""".stripMargin,
              href := s"$domain/unsubscribe?lang=${lang.language}&email=$email",
              tr"index.unsubscribe"
            )
          )
        )
      )
    )
  )