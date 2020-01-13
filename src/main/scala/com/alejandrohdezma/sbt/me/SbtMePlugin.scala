package com.alejandrohdezma.sbt.me

import scala.sys.process._

import sbt.Def.Setting
import sbt.Keys._
import sbt.plugins.JvmPlugin
import sbt.{settingKey, url, AutoPlugin, PluginTrigger, Plugins, SettingKey}

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

    val downloadInfoFromGithub: SettingKey[Boolean] =
      settingKey[Boolean]("Whether sbt-me should download information from Github or not")

  }

  import autoImport._

  override def trigger: PluginTrigger = allRequirements

  override def requires: Plugins = JvmPlugin

  override def projectSettings: Seq[Setting[_]] = Seq(
    downloadInfoFromGithub := sys.env.contains("RELEASE"),
    homepage := {
      if (downloadInfoFromGithub.value) Some(url(repository.url))
      else homepage.value
    },
    developers := {
      if (downloadInfoFromGithub.value) List()
      else developers.value
    },
    description := {
      if (downloadInfoFromGithub.value) repository.description
      else description.value
    },
    licenses := {
      if (downloadInfoFromGithub.value) repository.licenses
      else licenses.value
    }
  )

  private lazy val repository: Repository = Repository.get(user, repo).fold(sys.error, identity)

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
