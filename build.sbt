ThisBuild / scalaVersion := "2.12.10"
ThisBuild / organization := "com.alejandrohdezma"

Global / onChangedBuildSource := ReloadOnSourceChanges

lazy val `sbt-me` = project
  .in(file("."))
  .enablePlugins(SbtPlugin, MdocPlugin)
  .settings(
    libraryDependencies ++= Seq(
      "org.specs2" %% "specs2-core"         % "4.8.3"   % Test,
      "org.http4s" %% "http4s-dsl"          % "0.20.15" % Test,
      "org.http4s" %% "http4s-blaze-server" % "0.20.15" % Test
    ),
    mdocVariables := Map(
      "VERSION"       -> version.value.replaceAll("\\+.*", ""),
      "CONTRIBUTORS"  -> contributors.value.markdown,
      "COLLABORATORS" -> collaborators.value.markdown
    )
  )
