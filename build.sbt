ThisBuild / scalaVersion := "2.12.10"
ThisBuild / organization := "com.alejandrohdezma"

Global / onChangedBuildSource := ReloadOnSourceChanges

addCommandAlias("ci-test", "fix --check; mdoc; test; scripted")
addCommandAlias("ci-docs", "mdoc headerCreateAll")

lazy val root = project
  .in(file("."))
  .settings(name := "sbt-github")
  .aggregate(`sbt-github`, `sbt-github-mdoc`, `sbt-github-header`)
  .enablePlugins(MdocPlugin)
  .settings(skip in publish := true)
  .settings(mdocOut := file("."))
  .settings(
    mdocVariables ++= Map(
      "EXCLUDED_CONTRIBUTORS" -> excludedContributors.value.map(c => s"- $c").mkString("\n")
    )
  )

lazy val `sbt-github` = project
  .enablePlugins(SbtPlugin)
  .settings(scriptedLaunchOpts += "-Dplugin.version=" + version.value)
  .settings(
    libraryDependencies ++= Seq(
      "org.specs2"     %% "specs2-core"         % "4.8.3"   % Test,
      "org.http4s"     %% "http4s-dsl"          % "0.20.17" % Test,
      "org.http4s"     %% "http4s-blaze-server" % "0.20.17" % Test,
      "ch.qos.logback" % "logback-classic"      % "1.2.3"   % Test
    )
  )

lazy val `sbt-github-mdoc` = project
  .settings(description := "Provides most of the info downloaded by stb-github as mdoc variables")
  .enablePlugins(SbtPlugin)
  .settings(scriptedLaunchOpts += "-Dplugin.version=" + version.value)
  .dependsOn(`sbt-github`)
  .settings(addSbtPlugin("org.scalameta" % "sbt-mdoc" % "[2.0,)" % Provided))

lazy val `sbt-github-header` = project
  .settings(description := "Integration between sbt-github and sbt-header")
  .enablePlugins(SbtPlugin)
  .settings(scriptedLaunchOpts += "-Dplugin.version=" + version.value)
  .dependsOn(`sbt-github`)
  .settings(addSbtPlugin("de.heikoseeberger" % "sbt-header" % "[5.0,)" % Provided))
