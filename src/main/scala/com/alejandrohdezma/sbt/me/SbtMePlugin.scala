package com.alejandrohdezma.sbt.me

import scala.sys.process._

import sbt.Def.Setting
import sbt.Keys._
import sbt.plugins.JvmPlugin
import sbt.{settingKey, url, AutoPlugin, Def, PluginTrigger, Plugins}

import com.alejandrohdezma.sbt.me.github.Repository

/**
 * This plugin automatically enables reloading on sbt source changes and
 * adds POM-related settings like description, organization, license, homepage...
 *
 * All the settings values are downloaded from the repository and current user
 * information from the Github API.
 *
 * This will only happen during the release stage in Travis CI, since its only
 * needed during this phase.
 */
object SbtMePlugin extends AutoPlugin {

  object autoImport {

    val repository = settingKey[Option[Repository]] {
      "Repository information downloaded from Github"
    }

    val downloadInfoFromGithub = settingKey[Boolean] {
      "Whether sbt-me should download information from Github or not"
    }

  }

  import autoImport._

  override def trigger: PluginTrigger = allRequirements

  override def requires: Plugins = JvmPlugin

  override def globalSettings: Seq[Setting[_]] = Seq(
    downloadInfoFromGithub := sys.env.contains("RELEASE"),
    repository := {
      if (downloadInfoFromGithub.value)
        Some(Repository.get(user, repo).fold(sys.error, identity))
      else None
    },
    homepage := repository.value.map(r => url(r.url)).orElse(homepage.value),
    licenses := repository.value.map(_.licenses).getOrElse(licenses.value)
  )

  override def projectSettings: Seq[Def.Setting[_]] = Seq(
    description := repository.value.map(_.description).getOrElse(description.value)
  )

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

}
