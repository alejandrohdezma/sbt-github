ThisBuild / scalaVersion                  := "2.12.12"
ThisBuild / organization                  := "com.alejandrohdezma"
ThisBuild / pluginCrossBuild / sbtVersion := "1.2.8"

addCommandAlias("ci-test", "fix --check; mdoc; docusaurusCreateSite; publishLocal; scripted; testCovered")
addCommandAlias("ci-docs", "github; mdoc; headerCreateAll; website/docusaurusPublishGhpages")
addCommandAlias("ci-publish", "github; ci-release")

skip in publish := true

val `sbt-mdoc`   = "org.scalameta"     % "sbt-mdoc"   % "[2.0,)"   % Provided // scala-steward:off
val `sbt-header` = "de.heikoseeberger" % "sbt-header" % "[5.6.0,)" % Provided // scala-steward:off

lazy val documentation = project
  .enablePlugins(MdocPlugin)
  .settings(skip in publish := true)
  .settings(mdocOut := file("."))

lazy val website = project
  .enablePlugins(MdocPlugin, DocusaurusPlugin)
  .settings(skip in publish := true)
  .settings(mdocIn := baseDirectory.value / "docs")
  .settings(docusaurusProjectName := "sbt-github")
  .settings(watchTriggers += mdocIn.value.toGlob / "*.md")
  .settings(mdocVariables += "EXCLUDED" -> excludedContributors.value.mkString("- ", "\n- ", ""))

lazy val `sbt-github` = project
  .enablePlugins(SbtPlugin)
  .settings(scriptedLaunchOpts += s"-Dplugin.version=${version.value}")
  .settings(testFrameworks += new TestFramework("munit.Framework"))
  .settings(libraryDependencies += "org.scalameta" %% "munit" % "0.7.20" % Test)
  .settings(libraryDependencies += "org.http4s" %% "http4s-dsl" % "0.21.14" % Test)
  .settings(libraryDependencies += "org.http4s" %% "http4s-blaze-server" % "0.21.14" % Test)

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
