package com.alejandrohdezma.sbt.me

import cats.implicits._

import sbt.Def.Setting
import sbt.Keys._
import sbt.nio.Keys.{onChangedBuildSource, ReloadOnSourceChanges}
import sbt.plugins.JvmPlugin
import sbt.{url, AutoPlugin, Def, Developer, PluginTrigger, Plugins, SettingKey}

import com.alejandrohdezma.sbt.me.github.Repository

/**
 * This plugin automatically enables reloading on sbt source changes and
 * adds POM-related settings like description, organization, license, homepage...
 *
 * Both description and licenses are downloaded from the repository information
 * on github once the `repository` settings has been set.
 *
 * This will only happen during the release stage in Travis CI, since its only
 * needed during this phase.
 */
object SbtMePlugin extends AutoPlugin {

  override def trigger: PluginTrigger = allRequirements

  override def requires: Plugins = JvmPlugin

  object autoImport {

    val repository: SettingKey[String] =
      SettingKey[String]("repository", "Name of the repository where this project is hosted")

  }

  import autoImport._

  override def globalSettings: Seq[Def.Setting[_]] = Seq(
    onChangedBuildSource := ReloadOnSourceChanges
  )

  override def projectSettings: Seq[Setting[_]] =
    Seq(
      organization := "com.alejandrohdezma",
      homepage     := Some(url(s"${me.url}/${repository.value}")),
      developers   := List(me)
    ) ++ onReleaseStage(
      description := repo.value.map(_.description).orEmpty,
      licenses    := repo.value.map(_.licenses).orEmpty
    )

  private val repo: Def.Initialize[Option[Repository]] = Def.setting {
    repository.?.value.map { name =>
      val message = s"You forgot to set `$TOKEN` in Travis environment variables. " +
        s"Go to https://travis-ci.com/alejandrohdezma/$name/settings and add it."

      val repository = for {
        token      <- sys.env.get(TOKEN).toRight(message)
        repository <- github.api.retrieveRepository(name, token)
      } yield repository

      repository.fold(sys.error, identity)
    }
  }

  private def onReleaseStage(settings: Setting[_]*): Seq[Setting[_]] =
    if (sys.env.contains("RELEASE")) settings else Nil

  private val me = Developer(
    "alejandrohdezma",
    "Alejandro Hernández",
    "info@alejandrohdezma.com",
    url("https://github.com/alejandrohdezma")
  )

  private val TOKEN: String = "GITHUB_PERSONAL_ACCESS_TOKEN"

}
