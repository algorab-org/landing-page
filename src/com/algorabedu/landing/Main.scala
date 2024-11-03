package com.algorabedu.landing

import scalatags.Text.TypedTag

object Main extends cask.MainRoutes {

  @cask.staticResources("/public")
  def cssRoutes() = "public"
  
  @cask.get("/")
  def home(lang: Translation = Translation.load(Translation.FallbackLanguage)): TypedTag[String] =
    html.index(lang).toTag

  initialize()
}