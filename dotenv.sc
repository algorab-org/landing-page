import mill._, scalalib._

//File taken from https://github.com/vic/mill-dotenv

object DotEnvModule {

  def parse(pathRef: PathRef):Map[String,String] = {
    parse(os.read(pathRef.path))
  }

  def parse(source: String): Map[String, String] = {
    source.split("\n").flatMap(LINE_REGEX.findAllMatchIn)
      .map(keyValue => (keyValue.group(1), unescapeCharacters(removeQuotes(keyValue.group(2)))))
      .toMap
  }

  private def removeQuotes(value: String): String = {
    value.trim match {
      case quoted if quoted.startsWith("'") && quoted.endsWith("'") => quoted.substring(1, quoted.length - 1)
      case quoted if quoted.startsWith("\"") && quoted.endsWith("\"") => quoted.substring(1, quoted.length - 1)
      case unquoted => unquoted
    }
  }

  private def unescapeCharacters(value: String): String = {
    value.replaceAll("""\\([^$])""", "$1")
  }

  // shamefuly copied by mill-dotenv from SbtDotenv
  // https://github.com/mefellows/sbt-dotenv/blob/master/src/main/scala/au/com/onegeek/sbtdotenv/SbtDotenv.scala

  private val LINE_REGEX =
    "(?:^|^)\\s*(?:export\\s+)?([\\w.-]+)(?:\\s*=\\s*?|:\\s+?)(\\s*'(?:\\\\'|[^'])*'|\\s*\"(?:\\\\\"|[^\"])*\"|\\s*`(?:\\`|[^`])*`|[^#\r\n]+)?\\s*(?:#.*)?(?:$|$)".r
}

trait DotEnvModule extends JavaModule {

  def dotenvSources = T.sources { millModuleBasePath.value / ".env" }

  def dotenv = T.input {
    dotenvSources().map(DotEnvModule.parse).foldLeft(Map[String,String]()) { _ ++ _ }
  }

  override def forkEnv = super.forkEnv() ++ dotenv()
}