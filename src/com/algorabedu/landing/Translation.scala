package com.algorabedu.landing

import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.util.Properties
import scala.jdk.CollectionConverters.PropertiesHasAsScala
import scala.util.Using
import cask.router.ArgReader
import cask.endpoints.QueryParamReader

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
      val properties = Properties()
      val langResource = getClass.getResourceAsStream(s"/lang/$language.lang")

      if langResource == null then Map.empty
      else
        Using.resource(new InputStreamReader(langResource, StandardCharsets.UTF_8)): langUtf8 =>
          properties.load(langUtf8)
          properties.asScala.toMap

    Translation(language, entries, fallback)

  def load(language: String, fallbackLanguage: String): Translation =
    load(language, Some(load(fallbackLanguage)))
