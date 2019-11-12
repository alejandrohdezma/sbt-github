package com.alejandrohdezma.sbt.me

import scala.sys.process._

import sbt.Def.Setting
import sbt.Keys._
import sbt.nio.Keys.{onChangedBuildSource, ReloadOnSourceChanges}
import sbt.plugins.JvmPlugin
import sbt.{url, AutoPlugin, Def, Developer, PluginTrigger, Plugins}

import com.alejandrohdezma.sbt.me.github.Repository

/**
 * This plugin automatically enables reloading on sbt source changes and
 * adds POM-related settings like description, organization, license, homepage...
 *
 * Both description and licenses are downloaded from the repository information
 * on github.
 *
 * This will only happen during the release stage in Travis CI, since its only
 * needed during this phase.
 */
object SbtMePlugin extends AutoPlugin {

  override def trigger: PluginTrigger = allRequirements

  override def requires: Plugins = JvmPlugin

  override def globalSettings: Seq[Def.Setting[_]] = Seq(
    onChangedBuildSource := ReloadOnSourceChanges
  )

  override def projectSettings: Seq[Setting[_]] =
    Seq(
      organization := "com.alejandrohdezma",
      homepage     := Some(url(repository.value.html_url)),
      developers   := List(me)
    ) ++ onReleaseStage(
      description := repository.value.description,
      licenses    := repository.value.licenses
    )

  private val repository: Def.Initialize[Repository] = Def.setting {
    val message = s"You forgot to set `$TOKEN` in Travis environment variables. " +
      s"Go to https://travis-ci.com/alejandrohdezma/$name/settings and add it."

    val repository = for {
      token      <- sys.env.get(TOKEN).toRight(message)
      repository <- github.api.retrieveRepository(user, repo, token)
    } yield repository

    repository.fold(sys.error, identity)
  }

  /** Gets the Github user and repository from the git remote info */
  private lazy val (user, repo): (String, String) = {
    val identifier = """([^\/]+)"""

    val GithubHttps = s"https://github.com/$identifier/$identifier.git".r
    val GithubSsh   = s"git@github.com:$identifier/$identifier.git".r

    "git ls-remote --get-url origin".!!.trim() match {
      case GithubHttps(user, repo) => (user, repo)
      case GithubSsh(user, repo)   => (user, repo)
      case _                       => sys.error("Unable to get info from `git ls-remote --get-url origin`")
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
