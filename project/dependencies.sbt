// For using the plugins in their own build
Compile / unmanagedSourceDirectories ++= Seq(
  (ThisBuild / baseDirectory).value.getParentFile / "modules" / "sbt-github-mdoc" / "src" / "main" / "scala",
  (ThisBuild / baseDirectory).value.getParentFile / "modules" / "sbt-github-header" / "src" / "main" / "scala",
  (ThisBuild / baseDirectory).value.getParentFile / "modules" / "sbt-github" / "src" / "main" / "scala"
)

libraryDependencies += "org.typelevel" %% "jawn-parser" % "1.4.0"
