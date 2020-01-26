ThisBuild / scmInfo := Some(
  ScmInfo(url("http://example.com"), "scm:git:https://github.com/alejandrohdezma/sbt-me.git")
)
ThisBuild / downloadInfoFromGithub := true
ThisBuild / githubApiEntryPoint := {
  val github = baseDirectory.value / "github"

  github.listFiles.foreach { file =>
    val source  = scala.io.Source.fromFile(file)
    val content = source.mkString.replaceAllLiterally("{{base_directory}}", s"file://$github")

    source.close()

    val bw = new java.io.PrintWriter(file)
    bw.write(content)
    bw.close()
  }

  s"file://${github / "entrypoint.json"}"
}

lazy val root = project.in(file(".")).enablePlugins(MdocPlugin)
