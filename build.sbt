ThisBuild / scalaVersion := "2.12.10"

enablePlugins(SbtPlugin, MdocPlugin)

mdocVariables := Map("VERSION" -> version.value.replaceAll("\\+.*", ""))

libraryDependencies += "org.specs2" %% "specs2-core"         % "4.8.3"   % Test
libraryDependencies += "org.specs2" %% "specs2-cats"         % "4.8.3"   % Test
libraryDependencies += "org.http4s" %% "http4s-dsl"          % "0.20.15" % Test
libraryDependencies += "org.http4s" %% "http4s-blaze-server" % "0.20.15" % Test
