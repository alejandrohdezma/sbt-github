// Disable sbt-github downloading so we don't rely in API
ThisBuild / githubEnabled := false

ThisBuild / licenses  += "miau" -> url("http://localhost")
ThisBuild / yearRange := Some("2015-2020")
