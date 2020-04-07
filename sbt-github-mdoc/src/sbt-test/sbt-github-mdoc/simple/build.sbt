ThisBuild / scmInfo := Some(
  ScmInfo(url("http://example.com"), "scm:git:https://github.com/alejandrohdezma/sbt-github.git")
)
ThisBuild / downloadInfoFromGithub := true
ThisBuild / githubToken            := Token("1234")
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

  url(s"file://${github / "entrypoint.json"}")
}

lazy val root = project.in(file(".")).enablePlugins(MdocPlugin)
