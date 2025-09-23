package com.algorabedu.landing

import org.postgresql.ds.PGSimpleDataSource
import scalasql.DbClient
import scalasql.PostgresDialect.*
import scalasql.core.SqlStr
import scalasql.core.SqlStr.SqlStringSyntax
import scalatags.Text.TypedTag
import scalatags.Text.all.*
import java.nio.charset.StandardCharsets
import courier.*
import javax.mail.internet.InternetAddress
import scala.util.Success
import scala.util.Failure
import scala.concurrent.duration.DurationInt

object Main extends cask.MainRoutes:

  override val host: String = sys.env.getOrElse("HOST", "localhost")

  override val port: Int = sys.env.get("PORT").flatMap(_.toIntOption).getOrElse(8080)

  val domain = sys.env.getOrElse("MAIL_DOMAIN", s"http://$host:$port")

  val dataSource = PGSimpleDataSource()
  dataSource.setURL(sys.env("DATABASE_URL"))
  dataSource.setDatabaseName(sys.env("DATABASE_NAME"))
  dataSource.setUser(sys.env("DATABASE_USER"))
  dataSource.setPassword(sys.env("DATABASE_PASSWORD"))

  lazy val postgresClient = DbClient.DataSource(dataSource)

  val mailer =
    Mailer(sys.env("MAIL_HOST"), sys.env("MAIL_PORT").toInt)
      .debug(true)
      .auth(true)
      .as(sys.env("MAIL_USER"), sys.env("MAIL_PASSWORD"))
      .startTls(true)()

  @cask.staticResources("/public")
  def publicRoutes() = "public"
  
  @cask.get("/")
  def home(lang: Translation = Translation.load(Translation.FallbackLanguage)): TypedTag[String] =
    page.home(using lang)

  @cask.postForm("/subscribe")
  def subscribe(lang: Translation = Translation.load(Translation.FallbackLanguage), email: String) =
    val result = Email.option(email).fold(SubscriptionResult.InvalidEmail): _ =>
      try
        postgresClient.transaction: db =>
          if db.runRaw[Int]("SELECT count(*) FROM newsletter WHERE email = ?", Seq(email)).sum == 0 then
            db.updateRaw("INSERT INTO newsletter VALUES (?)", Seq(email))
            mailer(
              Envelope
                .from("newsletter" `@` "algorab.org")
                .to(InternetAddress(email))
                .subject(lang("newsletter.subscribe.subject"))
                .content(Multipart().html(mail.subscribed(email, domain)(using lang).render))
            )
            .onComplete:
              case Success(_) =>
              case Failure(exception) => exception.printStackTrace()

            SubscriptionResult.Subscribed
          else
            SubscriptionResult.AlreadySubscribed
      catch e =>
        e.printStackTrace()
        SubscriptionResult.MiscellaneousError

    ujson.Obj(
      "successful" -> result.successful,
      "message"    -> lang(result.translationKey)
    )

  private def unsubscribeMail(email: String): SubscriptionResult =
    Email.option(email).fold(SubscriptionResult.InvalidEmail): _ =>
      try
        postgresClient.transaction: db =>
          db.updateRaw("DELETE FROM newsletter WHERE email = ?", Seq(email))
          SubscriptionResult.Unsubscribed
      catch e =>
        e.printStackTrace()
        SubscriptionResult.MiscellaneousError

  @cask.postForm("/unsubscribe")
  def unsubscribe(lang: Translation = Translation.load(Translation.FallbackLanguage), email: String) =
    val result = unsubscribeMail(email)

    ujson.Obj(
      "successful" -> result.successful,
      "message"    -> lang(result.translationKey)
    )

  @cask.get("/unsubscribe")
  def unsubscribeGet(lang: Translation = Translation.load(Translation.FallbackLanguage), email: String) =
    if unsubscribeMail(email).successful then page.unsubscribe(email)(using lang)
    else page.home(using lang)

  @cask.get("/preview")
  def preview() =
    mail.subscribed("foo@bar.com", "localhost:8080")(using Translation.load("fr"))

  @cask.get("/privacy")
  def privacy(lang: Translation = Translation.load(Translation.FallbackLanguage)) =
    page.privacy(using lang)

  override def main(args: Array[String]): Unit =
    val initResource = os.resource / "init.sql"

    postgresClient.transaction: db =>
      db.updateRaw(os.read(initResource, StandardCharsets.UTF_8))

    //Preload page (current parser/highlighter is slow)
    page.home(using Translation("dummy", Map.empty, None))
    
    println(s"Listening to $host:$port")

    initialize()
    super.main(args)