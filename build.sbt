ThisBuild / scalaVersion := "2.12.10"

enablePlugins(SbtPlugin, MdocPlugin)

mdocVariables := Map("VERSION" -> version.value.replaceAll("\\+.*", ""))

libraryDependencies += "io.circe"   %% "circe-generic" % "0.12.3"
libraryDependencies += "io.circe"   %% "circe-parser"  % "0.12.3"
libraryDependencies += "org.scalaj" %% "scalaj-http"   % "2.4.2"
