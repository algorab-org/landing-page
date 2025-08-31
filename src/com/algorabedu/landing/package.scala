package com.algorabedu.landing

import io.github.iltotore.iron.*
import io.github.iltotore.iron.constraint.string.Match
import scalatags.Text.TypedTag
import java.io.OutputStream

implicit class TypedTagData(s: TypedTag[String])(implicit f: TypedTag[String] => geny.Writable) extends cask.Response.Data:
  val writable = f(s)
  def write(out: OutputStream) = writable.writeBytesTo(out)

  def headers =
    writable.httpContentType.map(tpe => "Content-Type" -> s"$tpe; charset=utf-8").toSeq ++
    writable.contentLength.map("Content-Length" -> _.toString)

private type EmailConstraint = DescribedAs[
  Match[
    "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])"
  ],
  "Should be a valid email"
]

opaque type Email = String :| EmailConstraint
object Email extends RefinedTypeOps[String, EmailConstraint, Email]

extension (sc: StringContext)
  def tr(args: Any*)(using translation: Translation): String =
    translation(sc.s(args*))