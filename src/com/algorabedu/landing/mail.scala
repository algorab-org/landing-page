package com.algorabedu.landing

import scalatags.Text.all.*
import scalatags.Text.TypedTag
import courier.Content
import courier.Multipart

def mailHtml(language: String, modifiers: Modifier*): TypedTag[String] =
  html(
    lang := language,
    body(
      modifiers
    )
  )

def mailContent(language: String, modifiers: Modifier*): Content =
  Multipart().html(mailHtml(language, modifiers*).render)