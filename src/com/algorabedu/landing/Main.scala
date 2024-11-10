package com.algorabedu.landing

import scalatags.Text.TypedTag

object Main extends cask.MainRoutes {

  @cask.staticResources("/public")
  def publicRoutes() = "public"
  
  @cask.get("/")
  def home(lang: Translation = Translation.load(Translation.FallbackLanguage)): TypedTag[String] =
    html.index(lang).toTag

  @cask.postForm("/subscribe")
  def subscribe(lang: Translation = Translation.load(Translation.FallbackLanguage), email: String) =
    val result = SubscriptionResult.Subscribed

    ujson.Obj(
      "successful" -> result.successful,
      "message" -> lang(result.translationKey)
    )

  initialize()
}