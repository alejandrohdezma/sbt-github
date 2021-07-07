// Disable sbt-github downloading so we don't rely in API
ThisBuild / githubEnabled := false

ThisBuild / licenses         += "Apache-2.0" -> url("http://localhost")
ThisBuild / yearRange        := Some("2015-2020")

copyrightOwner   := "My Company"
