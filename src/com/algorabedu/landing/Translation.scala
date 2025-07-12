package com.algorabedu.landing

import cask.endpoints.QueryParamReader
import cask.router.ArgReader
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.util.Properties
import scala.jdk.CollectionConverters.PropertiesHasAsScala
import scala.util.Using

case class Translation(language: String, entries: Map[String, String], fallback: Option[Translation]):

  def apply(key: String): String = entries
    .get(key)
    .orElse(fallback.flatMap(_.entries.get(key)))
    .getOrElse(key)

object Translation:

  val FallbackLanguage: String = "en"

  given QueryParamReader[Translation] = QueryParamReader.SimpleParam(Translation.load(_, FallbackLanguage))

  def load(language: String, fallback: Option[Translation] = None): Translation =
    val entries =
      val langResource = os.resource / "lang" / s"$language.lang"
      val langStream = langResource.getInputStream

      if langStream == null then Map.empty
      else
        Using.resource(InputStreamReader(langStream, StandardCharsets.UTF_8)): data =>
          val properties = Properties()
          properties.load(data)
          properties.asScala.toMap

    Translation(language, entries, fallback)

  def load(language: String, fallbackLanguage: String): Translation =
    load(language, Some(load(fallbackLanguage)))
