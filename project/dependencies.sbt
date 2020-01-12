// For using the plugins in their own build
unmanagedSourceDirectories in Compile +=
  baseDirectory.in(ThisBuild).value.getParentFile / "src" / "main" / "scala"
