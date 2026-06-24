// Disable sbt-github downloading so we don't rely in API
ThisBuild / githubEnabled := false

ThisBuild / licenses += com.alejandrohdezma.sbt.github.PluginCompat.license("Apache-2.0", new java.net.URI("http://localhost"))
ThisBuild / yearRange := Some("2015-2020")

copyrightOwner := "My Company"
