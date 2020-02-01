// Disable sbt-github downloading so we don't rely in API
ThisBuild / downloadInfoFromGithub := false

ThisBuild / licenses += "MIT" -> url("http://localhost")