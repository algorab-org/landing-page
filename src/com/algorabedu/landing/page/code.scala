package com.algorabedu.landing.page

import zio.parser.*
import scalatags.Text.all.*
import scalatags.Text.TypedTag
import scala.collection.mutable.ListBuffer
import zio.Chunk
import zio.ChunkBuilder

def prefixedCode(modifiers: Modifier*)(lines: (String, Modifier)*): TypedTag[String] =
  div(
    cls := "mockup-code",
    modifiers,
    lines.map((prefix, line) =>
      pre(
        dataPrefix := prefix,
        code(line)
      )
    )
  )

def prefixedCode(lines: (String, Modifier)*): TypedTag[String] =
  prefixedCode()(lines.map((a, b) => (a, b: Modifier))*)

def numberedCode(modifiers: Modifier*)(text: String, highlight: Boolean = true): TypedTag[String] =
  prefixedCode(modifiers)(
    (
      if highlight then highlightCode(text)
      else Chunk.from(text.split("\n")).map(x => (x: Modifier))
    )
      .zipWithIndex
      .map((l, i) => ((i + 1).toString, l))*
  )

def numberedCode(text: String): TypedTag[String] = numberedCode()(text, highlight = true)

def unprefixedCode(modifiers: Modifier*)(text: String): TypedTag[String] =
  prefixedCode(modifiers)(text.split("\n").map(("", _))*)

def unprefixedCode(text: String): TypedTag[String] = unprefixedCode()(text)

def highlightCode(text: String): Chunk[Modifier] =
  fullParser.parseString(text).getOrElse(Chunk(text))

private val keywords: List[String] = List(
  "and",
  "can",
  "case",
  "class",
  "cov",
  "contrav",
  "def",
  "else",
  "enum",
  "for",
  "if",
  "match",
  "record",
  "test",
  "then",
  "this",
  "val",
  "with",
  "while",
)

private val parens: List[Char] = List(
  '(',
  '[',
  '{',
  ')',
  ']',
  '}'
)

private def splitChunk(chunk: Chunk[Char], sep: Char): Chunk[Chunk[Char]] =
  val result = ChunkBuilder.make[Chunk[Char]]()
  val buffer = ChunkBuilder.make[Char]()

  for c <- chunk do
    if c == sep then
      result += buffer.result()
      buffer.clear()
    else buffer += c

  result += buffer.result()

  result.result()

private def mergeLines(linesA: Chunk[Modifier], linesB: Chunk[Modifier]): Chunk[Modifier] =
  linesA match
    case initA :+ lastA =>
      linesB match
        case firstB +: tailA =>
          (initA :+ (Chunk(lastA, firstB): Modifier)) ++ tailA 
        case _ => linesA
    case _ => linesB

type HighlightParser[+A] = Parser[String, Char, A]

extension [Err, In, A](parser: Parser[Err, In, A])
  def debug(name: String): Parser[Err, In, A] =
    parser.map { x => println(s"$name: $x"); x }

private val newlineParser: HighlightParser[Unit] =
  Parser.string("\r\n", ())
    .orElse(Parser.charIn('\r', '\n').unit)

val numberParser: HighlightParser[Modifier] =
  Parser.digit.repeat
    .zip(Parser.char('.').zip(Parser.digit.repeat0).optional)
    .map:
      case (partInt, None) => span(cls := "code-number", partInt.mkString)
      case (partInt, Some(partDec)) => span(cls := "code-number", s"${partInt.mkString}.${partDec.mkString}")

val parenParser: HighlightParser[Modifier] =
  Parser.charIn(parens*).map(paren => span(cls := "code-parenthesis", paren.toString))

val inlineCommentParser: HighlightParser[Modifier] =
  Parser.string("--", ())
    .zip(Parser.anyChar.repeatUntil(newlineParser))
    .map(comment => span(cls := "code-comment", s"--${comment.mkString}"))

private val escapeSequences = Map(
  'n' -> '\n',
  't' -> '\t',
  'r' -> '\r',
  'b' -> '\b',
  'f' -> '\f',
  '"' -> '"',
  '\\' -> '\\'
)

private val parseStringContent: Parser[String, Char, Chunk[Char]] =
  for
    ch <- Parser.anyChar
    result <-
      if ch == '\"' then Parser.succeed(Chunk.empty)
      else if ch == '\\' then
        Parser.anyChar.flatMap(ch2 =>
          escapeSequences
            .get(ch2)
            .fold(Parser.fail(s"Invalid escape sequence: \\$ch2"))(esc => parseStringContent.map(esc +: _))
        )
      else parseStringContent.map(ch +: _)
  yield result

val stringParser: HighlightParser[Modifier] =
  Parser.char('"')
    .zipRight(parseStringContent)
    .map(chunk => span(cls := "code-string", s"\"${chunk.mkString}\""))

val identParser: HighlightParser[Modifier] =
  Parser.letter
    .zip(Parser.alphaNumeric.repeat0)
    .map((first, chars) =>
      val text = s"$first${chars.mkString}"
      if keywords.contains(text) then 
        span(cls := "code-keyword", text)
      else if first.isUpper then 
        span(cls := "code-type", text)
      else
        text
    )

lazy val multilineCommentParser: HighlightParser[Chunk[Modifier]] =
  Parser.string("---", ())
    .zip(Parser.anyChar.repeatUntil(Parser.string("---", ())))
    .map: comment =>
      splitChunk(comment, '\n').map(_.mkString) match
        case head +: between :+ last =>
          span(cls := "code-comment", s"---$head")
          +: between.map(line => span(cls := "code-comment", line))
          :+ span(cls := "code-comment", s"$last---")

        case lines => lines.map(line => span(cls := "code-comment", line))
    .zip(fullParser.orElse(Parser.succeed(Chunk.empty)))
    .map(mergeLines)

lazy val inlineTokensParser: HighlightParser[Chunk[Modifier]] =
  identParser
    .orElse(numberParser)
    .orElse(parenParser)
    .orElse(stringParser)
    .orElse(Parser.anyChar.map(_.toString: Modifier))
    .repeatUntil(newlineParser)
    .map(x => x: Modifier)
    .flatMap(result =>
      inlineCommentParser
        .map(Chunk(result, _))
        .orElse(Parser.succeed(Chunk(result)))
        .flatMap(result2 =>
          Parser.end.not("EOF")
            .zip(fullParser.map(result2 ++ _))
            .orElse(Parser.succeed(result2))
        )
    )

lazy val fullParser: HighlightParser[Chunk[Modifier]] =
  multilineCommentParser
    .orElse(inlineTokensParser)