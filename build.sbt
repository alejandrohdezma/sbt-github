ThisBuild / scalaVersion                  := _root_.scalafix.sbt.BuildInfo.scala212
ThisBuild / organization                  := "com.alejandrohdezma"
ThisBuild / pluginCrossBuild / sbtVersion := "1.2.8"
ThisBuild / Test / parallelExecution      := false
ThisBuild / versionPolicyIntention        := Compatibility.BinaryAndSourceCompatible

addCommandAlias("ci-test", "fix --check; versionPolicyCheck; mdoc; test; publishLocal; scripted")
addCommandAlias("ci-docs", "github; mdoc; headerCreateAll; docusaurusPublishGhpages")
addCommandAlias("ci-publish", "versionCheck; github; ci-release")

val `sbt-mdoc` = "org.scalameta" % "sbt-mdoc" % "[2.0,)" % Provided // scala-steward:off

val `sbt-header` = "de.heikoseeberger" % "sbt-header" % "[5.6.0,)" % Provided // scala-steward:off

lazy val documentation = project.enablePlugins(MdocPlugin)

lazy val website = project
  .enablePlugins(MdocPlugin, DocusaurusPlugin)
  .settings(mdocIn := baseDirectory.value / "docs")
  .settings(mdocOut := (Compile / target).value / "mdoc")
  .settings(watchTriggers += mdocIn.value.toGlob / "*.md")
  .settings(mdocVariables += "EXCLUDED" -> excludedContributors.value.mkString("- ", "\n- ", ""))

lazy val `sbt-github` = module
  .enablePlugins(SbtPlugin)
  .settings(scriptedLaunchOpts += s"-Dplugin.version=${version.value}")
  .settings(libraryDependencies += "org.typelevel" %% "jawn-parser" % "1.5.1")
  .settings(libraryDependencies += "org.scalameta" %% "munit" % "0.7.29" % Test)
  .settings(libraryDependencies += "org.http4s" %% "http4s-dsl" % "0.23.26" % Test)
  .settings(libraryDependencies += "org.http4s" %% "http4s-blaze-server" % "0.23.16" % Test)

lazy val `sbt-github-mdoc` = module
  .enablePlugins(SbtPlugin)
  .dependsOn(`sbt-github`)
  .settings(description := "Provides most of the info downloaded by sbt-github as mdoc variables")
  .settings(scriptedLaunchOpts += s"-Dplugin.version=${version.value}")
  .settings(addSbtPlugin(`sbt-mdoc`))

lazy val `sbt-github-header` = module
  .enablePlugins(SbtPlugin)
  .dependsOn(`sbt-github`)
  .settings(description := "Integration between sbt-github and sbt-header")
  .settings(scriptedLaunchOpts += s"-Dplugin.version=${version.value}")
  .settings(addSbtPlugin(`sbt-header`))
