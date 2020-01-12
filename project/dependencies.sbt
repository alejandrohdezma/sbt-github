libraryDependencies += "io.circe" %% "circe-generic" % "0.12.3"
libraryDependencies += "io.circe" %% "circe-parser"  % "0.12.3"

// For using the plugins in their own build
unmanagedSourceDirectories in Compile +=
  baseDirectory.in(ThisBuild).value.getParentFile / "src" / "main" / "scala"
