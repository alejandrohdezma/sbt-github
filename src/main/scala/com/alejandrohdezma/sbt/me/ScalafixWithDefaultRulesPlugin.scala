package com.alejandrohdezma.sbt.me

import com.alejandrohdezma.sbt.me.github.api.copyRemoteConfigFile
import sbt.Keys._
import sbt._
import scalafix.sbt.ScalafixPlugin
import scalafix.sbt.ScalafixPlugin.autoImport._

/**
 * This plugin enables Scalafix plugin with several extra rules
 * and downloads configuration from remote url and stores it in
 * a `.scalafix.conf` file (remember to add this file to `.gitignore`
 * since it will be downloaded in each build).
 */
object ScalafixWithDefaultRulesPlugin extends AutoPlugin {

  private val scalafixRules: Seq[ModuleID] = Seq(
    "com.github.vovapolu" %% "scaluzzi"         % "0.1.3",
    "com.nequissimus"     %% "sort-imports"     % "0.3.0",
    "com.eed3si9n.fix"    %% "scalafix-noinfer" % "0.1.0-M1"
  )

  override def trigger: PluginTrigger = allRequirements

  override def requires: Plugins = ScalafixPlugin

  override def buildSettings: Seq[Setting[_]] = Seq(
    scalacOptions += "-P:semanticdb:synthetics:on",
    scalafixDependencies in ThisBuild ++= scalafixRules,
    addCompilerPlugin(scalafixSemanticdb),
    scalafixConfig := Some(copyRemoteConfigFile(".scalafix.conf", to = ".scalafix.conf"))
  )

}
