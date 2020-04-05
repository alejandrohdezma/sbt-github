ThisBuild / scalaVersion := "2.12.10"
ThisBuild / organization := "com.alejandrohdezma"

Global / onChangedBuildSource := ReloadOnSourceChanges

addCommandAlias("ci-test", "fix --check; mdoc; test; publishLocal; scripted")
addCommandAlias("ci-docs", "mdoc; headerCreateAll")

skip in publish := true

val `sbt-mdoc`   = "org.scalameta"     % "sbt-mdoc"   % "[2.0,)" % Provided // scala-steward:off
val `sbt-header` = "de.heikoseeberger" % "sbt-header" % "[5.0,)" % Provided // scala-steward:off

lazy val docs = project
  .in(file("sbt-github-docs"))
  .enablePlugins(MdocPlugin)
  .settings(skip in publish := true)
  .settings(mdocOut := file("."))
  .settings(mdocVariables += "EXCLUDED" -> excludedContributors.value.mkString("- ", "\n- ", ""))

lazy val `sbt-github` = project
  .enablePlugins(SbtPlugin)
  .settings(scriptedLaunchOpts += s"-Dplugin.version=${version.value}")
  .settings(libraryDependencies += "org.specs2" %% "specs2-core" % "4.9.2" % Test)
  .settings(libraryDependencies += "org.http4s" %% "http4s-dsl" % "0.21.3" % Test)
  .settings(libraryDependencies += "org.http4s" %% "http4s-blaze-server" % "0.21.3" % Test)
  .settings(libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.3" % Test)

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
