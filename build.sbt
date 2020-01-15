ThisBuild / scalaVersion := "2.12.10"
ThisBuild / organization := "com.alejandrohdezma"

Global / onChangedBuildSource := ReloadOnSourceChanges

lazy val `sbt-me` = project
  .in(file("."))
  .enablePlugins(SbtPlugin)
  .settings(
    libraryDependencies ++= Seq(
      "org.specs2"     %% "specs2-core"         % "4.8.3"   % Test,
      "org.http4s"     %% "http4s-dsl"          % "0.20.15" % Test,
      "org.http4s"     %% "http4s-blaze-server" % "0.20.15" % Test,
      "ch.qos.logback" % "logback-classic"      % "1.2.3"   % Test
    )
  )

lazy val `sbt-me-docs` = project
  .enablePlugins(MdocPlugin)
  .settings(skip in publish := true)
  .settings(
    mdocVariables := Map(
      "VERSION"       -> version.value.replaceAll("\\+.*", ""),
      "CONTRIBUTORS"  -> contributors.value.markdown,
      "COLLABORATORS" -> collaborators.value.markdown
    )
  )
