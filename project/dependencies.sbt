// For using the plugins in their own build
unmanagedSourceDirectories in Compile ++= Seq(
  baseDirectory.in(ThisBuild).value.getParentFile / "sbt-me-mdoc" / "src" / "main" / "scala",
  baseDirectory.in(ThisBuild).value.getParentFile / "sbt-me" / "src" / "main" / "scala"
)
