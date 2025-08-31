package com.algorabedu.landing.page

import scalatags.Text.TypedTag
import scalatags.Text.all.*
import scalatags.Text.svgAttrs.d
import scalatags.Text.svgAttrs.fill
import scalatags.Text.svgAttrs.rx
import scalatags.Text.svgAttrs.stroke
import scalatags.Text.svgAttrs.viewBox
import scalatags.Text.svgAttrs.x
import scalatags.Text.svgAttrs.y
import scalatags.Text.svgTags.g
import scalatags.Text.svgTags.path
import scalatags.Text.svgTags.rect
import scalatags.Text.svgTags.svg
import scalatags.text.Builder
import java.util.UUID

lazy val dataPrefix: Attr = attr("data-prefix")
lazy val dataTip: Attr = attr("data-tip")

val availableLanguages: Map[String, String] = Map(
  "fr" -> "🇫🇷",
  "en" -> "🇬🇧"
)

def alertSuccess(modifiers: Modifier*)(msg: String): TypedTag[String] =
  div(
    role := "alert",
    cls := "alert alert-success w-2xl",
    svg(
      xmlns := "http://www.w3.org/2000/svg",
      cls := "h-6 w-6 shrink-0 stroke-current",
      fill := "none",
      viewBox := "0 0 24 24",
      path(
        attr("stroke-linecap") := "round",
        attr("stroke-linejoin") := "round",
        attr("stroke-width") := "2",
        d := "M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"
      )
    ),
    span(msg),
    modifiers
  )

def alertError(modifiers: Modifier*)(msg: String): TypedTag[String] =
  div(
    role := "alert",
    cls := "alert alert-error",
    svg(
      xmlns := "http://www.w3.org/2000/svg",
      cls := "h-6 w-6 shrink-0 stroke-current",
      fill := "none",
      viewBox := "0 0 24 24",
      path(
        attr("stroke-linecap") := "round",
        attr("stroke-linejoin") := "round",
        attr("stroke-width") := "2",
        d := "M10 14l2-2m0 0l2-2m-2 2l-2-2m2 2l2 2m7-2a9 9 0 11-18 0 9 9 0 0118 0z"
      )
    ),
    span("Error! Task failed successfully."),
    modifiers
  )

def newsletterButton(text: String)(modifiers: Modifier*): TypedTag[String] =
  button(
    cls := "btn-newsletter swap swap-active",
    span(cls := "swap-on", text),
    span(cls := "swap-off loading loading-spinner place-self-center"),
    modifiers
  )

case class TabsScope(name: String)

def tabs(modifiers: TabsScope ?=> Modifier*): TypedTag[String] =
  val scope = TabsScope("tab_" + UUID.randomUUID().toString())
  div(
    cls := "tabs",
    modifiers.map(_(using scope))
  )

def radioTab(title: String)(modifiers: Modifier*)(using scope: TabsScope): TypedTag[String] =
  input(
    cls := "tab [--tab-border-color:gray]",
    tpe := "radio",
    name := scope.name,
    aria.label := title,
    modifiers
  )