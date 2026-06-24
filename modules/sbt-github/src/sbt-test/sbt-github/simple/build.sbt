import sbtcompat.PluginCompat._

ThisBuild / scmInfo := Some(
  ScmInfo(url("http://example.com"), "scm:git:https://github.com/alejandrohdezma/sbt-github.git")
)
ThisBuild / githubEnabled   := true
ThisBuild / githubAuthToken := Some(AuthToken("1234"))
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
  assert(description.value == "An awesome description")
  assert(organizationName.value == "The First User")
  assert(startYear.value.contains(2018))
  assert(yearRange.value.contains(s"2018-${java.time.Year.now.getValue}"))
  assert(homepage.value.map(_.toString).contains("https://github.com/user1/repo"))
  assert(organizationHomepage.value.map(_.toString).contains("https://github.com/user1"))
  assert(organizationEmail.value.contains("user1@example.com"))
  assert(
    com.alejandrohdezma.sbt.github.PluginCompat.licenseInfo(licenses.value) ==
      List("MIT" -> "https://api.github.com/licenses/mit")
  )
  assert(
    developers.value.map(developer => (developer.id, developer.name, developer.email, developer.url.toString)) == List(
      ("user1", "The First User", "user1@example.com", "https://github.com/user1"),
      ("user2", "The Second User", "", "https://github.com/user2")
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
  assert(releases.value.map(_.tag) == List("v1.0.0","v2.0.0","v2.1.0","v3.0.0"))
}
