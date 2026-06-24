// Disable sbt-github downloading so we don't rely in API
ThisBuild / githubEnabled := false

ThisBuild / licenses += com.alejandrohdezma.sbt.github.PluginCompat.license("MIT", new java.net.URI("http://localhost"))
ThisBuild / yearRange := Some("2015-2020")

organizationName := "My Company"
organizationHomepage := Some(url("http://example.com"))
