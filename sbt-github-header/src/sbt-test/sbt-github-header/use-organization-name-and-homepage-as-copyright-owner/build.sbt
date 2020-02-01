// Disable sbt-github downloading so we don't rely in API
ThisBuild / downloadInfoFromGithub := false

ThisBuild / licenses         += "MIT" -> url("http://localhost")
ThisBuild / yearRange        := Some("2015-2020")

organizationName := "My Company"
organizationHomepage := Some(url("http://example.com"))
