package com.algorabedu.landing

import play.twirl.api.HtmlFormat.Appendable
import scalatags.Text.all.{html, raw}
import scalatags.Text.TypedTag

extension (rawHtml: Appendable)
  
  def toTag: TypedTag[String] = html(raw(rawHtml.toString))