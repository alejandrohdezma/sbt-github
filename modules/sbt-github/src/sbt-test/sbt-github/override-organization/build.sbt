import sbtcompat.PluginCompat._

ThisBuild / scmInfo := Some(
  ScmInfo(url("http://example.com"), "scm:git:https://alejandrohdezma@github.com/alejandrohdezma/sbt-github.git")
)
ThisBuild / githubEnabled      := true
ThisBuild / githubOrganization := "different-org"
ThisBuild / githubAuthToken    := Some(AuthToken("1234"))
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

  (github / "entrypoint.json").toURI
}

TaskKey[Unit]("check", "Checks all the elements downloaded from the Github API are correct") := Def.uncached {
  assert(organizationName.value == "A Different Organization")
  assert(organizationHomepage.value.map(_.toString).contains("https://example.com/different"))
  assert(organizationEmail.value.contains("different@example.com"))
}
