// For using the plugins in their own build
unmanagedSourceDirectories in Compile ++= Seq(
  baseDirectory.in(ThisBuild).value.getParentFile / "modules" / "sbt-github-mdoc" / "src" / "main" / "scala",
  baseDirectory.in(ThisBuild).value.getParentFile / "modules" / "sbt-github-header" / "src" / "main" / "scala",
  baseDirectory.in(ThisBuild).value.getParentFile / "modules" / "sbt-github" / "src" / "main" / "scala"
)
