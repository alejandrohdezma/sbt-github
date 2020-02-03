ThisBuild / scalaVersion := "2.12.10"
ThisBuild / organization := "com.alejandrohdezma"

Global / onChangedBuildSource := ReloadOnSourceChanges

addCommandAlias("ci-test", "fix --check; mdoc; test; publishLocal; all scripted")
addCommandAlias("ci-docs", "mdoc; headerCreateAll")

lazy val root = project
  .in(file("."))
  .enablePlugins(MdocPlugin)
  .aggregate(`sbt-github`, `sbt-github-mdoc`, `sbt-github-header`)
  .settings(name := "sbt-github")
  .settings(skip in publish := true)
  .settings(mdocOut := file("."))
  .settings(mdocVariables += "EXCLUDED" -> excludedContributors.value.mkString("- ", "\n- ", ""))

lazy val `sbt-github` = project
  .enablePlugins(SbtPlugin)
  .settings(scriptedLaunchOpts += s"-Dplugin.version=${version.value}")
  .settings(libraryDependencies += "org.specs2" %% "specs2-core" % "4.8.3" % Test)
  .settings(libraryDependencies += "org.http4s" %% "http4s-dsl" % "0.20.17" % Test)
  .settings(libraryDependencies += "org.http4s" %% "http4s-blaze-server" % "0.20.17" % Test)
  .settings(libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.3" % Test)

lazy val `sbt-github-mdoc` = project
  .enablePlugins(SbtPlugin)
  .dependsOn(`sbt-github`)
  .settings(description := "Provides most of the info downloaded by stb-github as mdoc variables")
  .settings(scriptedLaunchOpts += s"-Dplugin.version=${version.value}")
  .settings(addSbtPlugin("org.scalameta" % "sbt-mdoc" % "[2.0,)" % Provided))

lazy val `sbt-github-header` = project
  .enablePlugins(SbtPlugin)
  .dependsOn(`sbt-github`)
  .settings(description := "Integration between sbt-github and sbt-header")
  .settings(scriptedLaunchOpts += s"-Dplugin.version=${version.value}")
  .settings(addSbtPlugin("de.heikoseeberger" % "sbt-header" % "[5.0,)" % Provided))
