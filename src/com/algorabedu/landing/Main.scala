package com.algorabedu.landing

import org.postgresql.ds.PGSimpleDataSource
import scalasql.DbClient
import scalasql.PostgresDialect.*
import scalasql.core.SqlStr
import scalasql.core.SqlStr.SqlStringSyntax
import scalatags.Text.TypedTag
import java.nio.charset.StandardCharsets
import com.algorabedu.Email

object Main extends cask.MainRoutes:

  val dataSource = PGSimpleDataSource()
  dataSource.setURL(sys.env("DATABASE_URL"))
  dataSource.setDatabaseName(sys.env("DATABASE_NAME"))
  dataSource.setUser(sys.env("DATABASE_USER"))
  dataSource.setPassword(sys.env("DATABASE_PASSWORD"))

  lazy val postgresClient = DbClient.DataSource(dataSource)

  @cask.staticResources("/public")
  def publicRoutes() = "public"
  
  @cask.get("/")
  def home(lang: Translation = Translation.load(Translation.FallbackLanguage)): TypedTag[String] =
    html.index(lang).toTag

  @cask.postForm("/subscribe")
  def subscribe(lang: Translation = Translation.load(Translation.FallbackLanguage), email: String) =
    val result = Email.option(email).fold(SubscriptionResult.InvalidEmail): _ =>
      try
        postgresClient.transaction: db =>
          if db.runRaw[Int]("SELECT count(*) FROM newsletter WHERE email = ?", Seq(email)).sum == 0 then
            db.updateRaw("INSERT INTO newsletter VALUES (?)", Seq(email))
            SubscriptionResult.Subscribed
          else
            SubscriptionResult.AlreadySubscribed
      catch e =>
        e.printStackTrace()
        SubscriptionResult.MiscellaneousError

    ujson.Obj(
      "successful" -> result.successful,
      "message" -> lang(result.translationKey)
    )

  override def main(args: Array[String]): Unit =
    val initResource = os.resource / "init.sql"

    postgresClient.transaction: db =>
      db.updateRaw(os.read(initResource, StandardCharsets.UTF_8))

    initialize()
    super.main(args)