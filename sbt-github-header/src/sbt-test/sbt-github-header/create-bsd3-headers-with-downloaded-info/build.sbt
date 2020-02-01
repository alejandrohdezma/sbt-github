// Disable sbt-github downloading so we don't rely in API
ThisBuild / downloadInfoFromGithub := false

ThisBuild / licenses         += "BSD-3-Clause" -> url("http://localhost")
ThisBuild / yearRange        := Some("2015-2020")

copyrightOwner   := "My Company"
