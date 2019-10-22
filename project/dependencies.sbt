libraryDependencies += "io.circe"   %% "circe-generic" % "0.12.3"
libraryDependencies += "io.circe"   %% "circe-parser"  % "0.12.3"
libraryDependencies += "org.scalaj" %% "scalaj-http"   % "2.4.2"

// For using the plugins in their own build
unmanagedSourceDirectories in Compile +=
  baseDirectory.in(ThisBuild).value.getParentFile / "src" / "main" / "scala"
