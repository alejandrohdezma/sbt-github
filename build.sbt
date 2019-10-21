ThisBuild / scalaVersion := "2.12.10"
ThisBuild / repository   := "sbt-me"
ThisBuild / name         := "sbt-me"

enablePlugins(SbtPlugin)

addSbtPlugin("com.geirsson"              % "sbt-ci-release" % "1.4.31")
addSbtPlugin("io.github.davidgregory084" % "sbt-tpolecat"   % "0.1.8")
addSbtPlugin("ch.epfl.scala"             % "sbt-scalafix"   % "0.9.7")
addSbtPlugin("org.scalameta"             % "sbt-scalafmt"   % "2.0.7")

libraryDependencies += "io.circe"   %% "circe-generic" % "0.12.2"
libraryDependencies += "io.circe"   %% "circe-parser"  % "0.12.2"
libraryDependencies += "org.scalaj" %% "scalaj-http"   % "2.4.2"
