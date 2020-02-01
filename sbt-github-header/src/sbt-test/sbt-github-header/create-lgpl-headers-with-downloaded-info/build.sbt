// Disable sbt-github downloading so we don't rely in API
ThisBuild / downloadInfoFromGithub := false

ThisBuild / licenses         += "LGPL-3.0" -> url("http://localhost")
ThisBuild / yearRange        := Some("2015-2020")

copyrightOwner   := "My Company"
