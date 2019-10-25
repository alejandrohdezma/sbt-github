package com.alejandrohdezma.sbt.me

import sbt.Keys.compile
import sbt.plugins.JvmPlugin
import sbt.{
  inAnyConfiguration,
  taskKey,
  AutoPlugin,
  Def,
  PluginTrigger,
  Plugins,
  ScopeFilter,
  TaskKey
}

import xsbti.compile.CompileAnalysis

/**
 * Enables the `compileAll` task for all projects, which launches compilation
 * of all configurations in which `compile` is enabled.
 */
object CompileAllPlugin extends AutoPlugin {

  override def requires: Plugins = JvmPlugin

  override def trigger: PluginTrigger = allRequirements

  object autoImport {

    val compileAll: TaskKey[Seq[Option[CompileAnalysis]]] = taskKey {
      "Execute the compile task for all configurations in which it is enabled."
    }

  }

  import autoImport._

  override def projectSettings: Seq[Def.Setting[_]] =
    Seq(compileAll := compile.?.all(configurations).value)

  private lazy val configurations = ScopeFilter(configurations = inAnyConfiguration)

}
