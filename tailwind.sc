import mill._, scalalib._

trait TailwindModule extends ScalaModule {

  def tailwindVersion: T[String]
  def tailwindCSSInput: T[PathRef] = T.source(PathRef(millModuleBasePath.value / "css" / "input.css"))
  def tailwindSources: T[Seq[PathRef]]
  def tailwindSubdirectory: T[String] = "public"

  def tailwindExecutablePath: T[PathRef] = T {
    val systemOsName = System.getProperty("os.name").toLowerCase()
    val osName =
      if(systemOsName.contains("windows")) "windows"
      else if(systemOsName.contains("mac")) "macos"
      else "linux"

    val osArch = System.getProperty("os.arch") match {
      case "amd64" | "x86_64" | "x64" => "x64"
      case "aarch64" | "arm64" => "arm64"
      case arch => arch
    }

    val osExtension = osName match {
      case "windows" => ".exe"
      case _ => ""
    }

    val exeName = s"tailwindcss-$osName-$osArch$osExtension"

    PathRef(T.dest / "tailwindcss")
  }

  def tailwindExecutableArtifact: T[String] = T {
    val systemOsName = System.getProperty("os.name").toLowerCase()
    val osName =
      if(systemOsName.contains("windows")) "windows"
      else if(systemOsName.contains("mac")) "macos"
      else "linux"

    val osArch = System.getProperty("os.arch") match {
      case "amd64" | "x86_64" | "x64" => "x64"
      case "aarch64" | "arm64" => "arm64"
      case arch => arch
    }

    val osExtension = osName match {
      case "windows" => ".exe"
      case _ => ""
    }

    s"tailwindcss-$osName-$osArch$osExtension"
  }

  def tailwindExecutable: T[PathRef] = T {
    val exePath = T.dest / "tailwindcss"

    os.write(
      exePath,
      requests.get.stream(s"https://github.com/tailwindlabs/tailwindcss/releases/download/v${tailwindVersion()}/${tailwindExecutableArtifact()}")
    )

    os.perms.set(exePath, "rwxrwxrwx")

    PathRef(exePath)
  }

  def tailwindCSSOutput: T[PathRef] = T {
    val outputPath = T.dest / tailwindSubdirectory() / "output.css"
    tailwindSources()
    os.proc(tailwindExecutable().path, "-i", tailwindCSSInput().path, "-o", outputPath).call(millModuleBasePath.value)
    PathRef(T.dest)
  }
}