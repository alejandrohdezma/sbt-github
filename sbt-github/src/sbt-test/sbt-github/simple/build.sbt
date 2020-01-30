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

  s"file://${github / "entrypoint.json"}"
}

TaskKey[Unit]("check", "Checks all the elements downloaded from the Github API are correct") := {
  assert(description.value == "An awesome description")
  assert(organizationName.value == "The First User")
  assert(startYear.value.contains(2018))
  assert(yearRange.value.contains("2018-2020"))
  assert(homepage.value.contains(url("https://github.com/user1/repo")))
  assert(organizationHomepage.value.contains(url("https://github.com/user1")))
  assert(organizationEmail.value.contains("user1@example.com"))
  assert(licenses.value == List("MIT" -> url("https://api.github.com/licenses/mit")))
  assert(
    developers.value == List(
      Developer("user1", "The First User", "user1@example.com", url("https://github.com/user1")),
      Developer("user2", "The Second User", "", url("https://github.com/user2"))
    )
  )
  assert(
    contributors.value.markdown ==
      """|- [![user1](http://example.com/user1.png&s=20) **user1**](https://github.com/user1)
         |- [![user3](http://example.com/user3.png&s=20) **user3**](https://github.com/user3)
         |- [![user2](http://example.com/user2.png&s=20) **user2**](https://github.com/user2)""".stripMargin
  )
  assert(
    collaborators.value.markdown ==
      """|- [![user1](http://example.com/user1.png&s=20) **The First User (user1)**](https://github.com/user1)
         |- [![user2](http://example.com/user2.png&s=20) **The Second User (user2)**](https://github.com/user2)""".stripMargin
  )
}
