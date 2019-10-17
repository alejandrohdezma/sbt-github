package com.alejandrohdezma.sbt.me

import com.alejandrohdezma.sbt.me.github.api.copyRemoteConfigFile
import org.scalafmt.sbt.ScalafmtPlugin
import org.scalafmt.sbt.ScalafmtPlugin.autoImport.scalafmtConfig
import sbt.{AutoPlugin, PluginTrigger, Plugins, Setting}

/**
 * This plugin downloads scalafmt configuration and stores it in
 * a `.scalafmt.conf` file (remember to add this file to `.gitignore`
 * * since it will be downloaded in each build).
 */
object ScalafmtWithDefaultConfigPlugin extends AutoPlugin {

  override def trigger: PluginTrigger = allRequirements

  override def requires: Plugins = ScalafmtPlugin

  override def buildSettings: Seq[Setting[_]] = Seq(
    scalafmtConfig := copyRemoteConfigFile(".scalafmt.conf", to = ".scalafmt.conf")
  )

}
