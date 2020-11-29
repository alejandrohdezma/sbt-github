ThisBuild / scalaVersion                  := "2.12.12"
ThisBuild / organization                  := "com.alejandrohdezma"
ThisBuild / pluginCrossBuild / sbtVersion := "1.2.8"

addCommandAlias("ci-test", "fix --check; mdoc; publishLocal; scripted; testCovered")
addCommandAlias("ci-docs", "github; mdoc; headerCreateAll; publishMicrosite")
addCommandAlias("ci-publish", "github; ci-release")

skip in publish := true

val `sbt-mdoc`   = "org.scalameta"     % "sbt-mdoc"   % "[2.0,)"   % Provided // scala-steward:off
val `sbt-header` = "de.heikoseeberger" % "sbt-header" % "[5.6.0,)" % Provided // scala-steward:off

lazy val documentation = project
  .enablePlugins(MdocPlugin)
  .settings(skip in publish := true)
  .settings(mdocOut := file("."))

lazy val microsite = project
  .enablePlugins(MdocPlugin, MicrositesPlugin)
  .settings(skip in publish := true)
  .settings(mdocVariables += "EXCLUDED" -> excludedContributors.value.mkString("- ", "\n- ", ""))
  .settings(micrositeName := name.in(`sbt-github`).value)
  .settings(micrositeDescription := description.in(`sbt-github`).value)
  .settings(micrositeGithubToken := githubAuthToken.value.map(_.value))
  .settings(micrositeBaseUrl := name.in(`sbt-github`).value)
  .settings(micrositePushSiteWith := GitHub4s)

lazy val `sbt-github` = project
  .enablePlugins(SbtPlugin)
  .settings(scriptedLaunchOpts += s"-Dplugin.version=${version.value}")
  .settings(testFrameworks += new TestFramework("munit.Framework"))
  .settings(libraryDependencies += "org.scalameta" %% "munit" % "0.7.15" % Test)
  .settings(libraryDependencies += "org.http4s" %% "http4s-dsl" % "0.21.13" % Test)
  .settings(libraryDependencies += "org.http4s" %% "http4s-blaze-server" % "0.21.13" % Test)

lazy val `sbt-github-mdoc` = project
  .enablePlugins(SbtPlugin)
  .dependsOn(`sbt-github`)
  .settings(description := "Provides most of the info downloaded by sbt-github as mdoc variables")
  .settings(scriptedLaunchOpts += s"-Dplugin.version=${version.value}")
  .settings(addSbtPlugin(`sbt-mdoc`))

lazy val `sbt-github-header` = project
  .enablePlugins(SbtPlugin)
  .dependsOn(`sbt-github`)
  .settings(description := "Integration between sbt-github and sbt-header")
  .settings(scriptedLaunchOpts += s"-Dplugin.version=${version.value}")
  .settings(addSbtPlugin(`sbt-header`))
