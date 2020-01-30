ThisBuild / scmInfo := Some(
  ScmInfo(url("http://example.com"), "scm:git:https://github.com/alejandrohdezma/sbt-github.git")
)
ThisBuild / extraCollaborators     += Collaborator.github("user3")
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

  s"file://${github / "entrypoint.json"}"
}

TaskKey[Unit]("check", "Checks all the elements downloaded from the Github API are correct") := {
  assert(
    collaborators.value.markdown ==
      """|- [![user1](http://example.com/user1.png&s=20) **The First User (user1)**](https://github.com/user1)
         |- [![user2](http://example.com/user2.png&s=20) **The Second User (user2)**](https://github.com/user2)
         |- [![user3](http://example.com/user3.png&s=20) **The Third User (user3)**](https://github.com/user3)""".stripMargin
  )
}
