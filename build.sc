import mill._, scalalib._

object main extends RootModule with ScalaModule {

  def scalaVersion = "3.5.0"

  def ivyDeps = Agg(
    ivy"com.lihaoyi::cask:0.9.4"
  )
}