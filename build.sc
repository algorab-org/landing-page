import $ivy.`com.lihaoyi::mill-contrib-twirllib:`
import $file.`dotenv`
import $file.`tailwind`

import mill._, scalalib._, twirllib._, dotenv._, tailwind._

object main extends RootModule with ScalaModule with TwirlModule with TailwindModule with DotEnvModule {

  def scalaVersion = "3.5.1"
  def twirlVersion = "1.6.6"
  def tailwindVersion = "3.4.13"

  def tailwindSources = twirlSources

  def generatedSources = T{ Seq(compileTwirl().classes) }

  def resources = super.resources() :+ tailwindCSSOutput()

  def ivyDeps = Agg(
    ivy"com.lihaoyi::cask:0.9.2",
    ivy"com.lihaoyi::scalatags:0.13.1",
    ivy"com.typesafe.play::twirl-api:${twirlVersion()}",
    ivy"io.github.iltotore::iron:2.6.0",
    ivy"io.github.iltotore::iron-doobie:2.6.0"
  )
}