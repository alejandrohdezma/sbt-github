ThisBuild / scmInfo := Some(
  ScmInfo(url("http://example.com"), "scm:git:https://github.com/alejandrohdezma/sbt-github.git")
)
ThisBuild / githubEnabled                 := true
ThisBuild / githubToken                   := Token("1234")
ThisBuild / populateOrganizationWithOwner := false
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

TaskKey[Unit]("check", "Checks all the elements downloaded from the Github API are correct") := {
  assert(organizationName.value == "default")
  assert(organizationHomepage.value.contains(url("https://github.com/user1/repo")))
  assert(organizationEmail.value.isEmpty)
}
