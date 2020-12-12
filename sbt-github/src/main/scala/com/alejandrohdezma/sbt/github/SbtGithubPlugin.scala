/*
 * Copyright 2019-2020 Alejandro Hernández <https://github.com/alejandrohdezma>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alejandrohdezma.sbt.github

import java.time.Year

import scala.language.postfixOps

import sbt.Def
import sbt.Def.Setting
import sbt.Keys._
import sbt._
import sbt.plugins.JvmPlugin

import com.alejandrohdezma.sbt.github.github.Organization
import com.alejandrohdezma.sbt.github.github.Repository
import com.alejandrohdezma.sbt.github.github.urls.GithubEntryPoint
import com.alejandrohdezma.sbt.github.syntax.list._

/**
 * This plugin automatically adds POM-related settings like description,
 * organization, license, homepage...
 *
 * All the settings values are downloaded from the repository and current user
 * information from the Github API.
 */
@SuppressWarnings(Array("scalafix:DisableSyntax.=="))
object SbtGithubPlugin extends AutoPlugin {

  object autoImport extends SbtGithubKeys

  import autoImport._

  override def trigger: PluginTrigger = allRequirements

  override def requires: Plugins = JvmPlugin

  override def buildSettings: Seq[Setting[_]] =
    aliases ++ Seq(
      githubApiEntryPoint           := url("https://api.github.com"),
      githubEnabled                 := false,
      populateOrganizationWithOwner := true,
      githubOrganization            := "",
      excludedContributors          := List("scala-steward", """.*\[bot\]""", "traviscibot"),
      extraCollaborators            := List(),
      githubAuthToken               := sys.env.get("GITHUB_TOKEN").map(AuthToken),
      repository := onGithub(default = Option.empty[Repository])(Def.setting {
        implicit val (auth, logger, url) = configuration.value
        Option(Repository.get(info.value._1, info.value._2).get) // scalafix:ok Disable.Try.get
      }).value,
      organizationMetadata := onRepo(default = Option.empty[Organization])(Def.setting { repo =>
        implicit val (auth, logger, url) = configuration.value

        if (githubOrganization.value.nonEmpty)
          Some(Organization.get(githubOrganization.value).get)
        else
          repo.organization.orElse {
            if (populateOrganizationWithOwner.value)
              Some(repo.owner.map(_.asOrganization))
            else None
          }.map(_.get) // scalafix:ok Disable.Try.get
      }).value,
      contributors := onRepo(default = Contributors(Nil))(Def.setting { repo =>
        implicit val (auth, log, _) = configuration.value
        repo.contributors(excludedContributors.value).get // scalafix:ok Disable.Try.get
      }).value,
      collaborators := onRepo(default = Collaborators(Nil))(Def.setting { repo =>
        implicit val (auth, log, entryPoint) = configuration.value

        val contributorIds = contributors.value.list.map(_.login)

        val collaborators = for {
          extras        <- extraCollaborators.value.traverse(_(auth)(entryPoint)(log))
          collaborators <- repo.collaborators(contributorIds)
        } yield collaborators.include(extras)

        collaborators.get // scalafix:ok Disable.Try.get
      }).value,
      developers := collaborators.value.developers,
      homepage   := repository.value.map(_.url).orElse(homepage.value),
      licenses   := repository.value.map(_.licenses).getOrElse(licenses.value),
      startYear  := repository.value.map(_.startYear).orElse(startYear.value),
      yearRange := startYear.value.collect {
        case start if start == Year.now.getValue => s"$start"
        case start                               => s"$start-${Year.now.getValue}"
      }
    )

  override def projectSettings: Seq[Def.Setting[_]] =
    Seq(
      description := repository.value.map(_.description).getOrElse(description.value),
      organizationName := organizationMetadata.value
        .flatMap(_.name)
        .getOrElse(organizationName.value),
      organizationHomepage := organizationMetadata.value.fold(organizationHomepage.value)(_.url),
      organizationEmail    := organizationMetadata.value.flatMap(_.email)
    )

  private[github] val configuration = Def.setting {
    (
      githubAuthToken.value.getOrElse(
        sys.error {
          "You need to add an environment variable named GITHUB_TOKEN with a Github Personal Access Token"
        }
      ),
      sLog.value,
      GithubEntryPoint(githubApiEntryPoint.value)
    )
  }

  /** Gets the Github user and repository from the git remote info */
  private[github] val info = Def.setting {
    val identifier = """([^\/]+)"""

    val Connection      = s"scm:git:https://github.com/$identifier/$identifier.git".r
    val ConnectionLogin = s"scm:git:https://$identifier@github.com/$identifier/$identifier.git".r

    scmInfo.value.map(_.connection) match {
      case Some(Connection(owner, repo))         => (owner, repo)
      case Some(ConnectionLogin(_, owner, repo)) => (owner, repo)
      case None                                  => sys.error("`scmInfo` is mandatory for this plugin to work")
      case Some(s)                               => sys.error(s"Invalid `scmInfo` connection value: $s")
    }
  }

  private lazy val aliases: Seq[Setting[State => State]] = Seq(
    "github"    -> ";set githubEnabled in ThisBuild := true",
    "githubOn"  -> ";set githubEnabled in ThisBuild := true",
    "githubOff" -> ";set githubEnabled in ThisBuild := false"
  ).flatMap(addCommandAlias _ tupled)

  private def onGithub[A](default: A)(f: Def.Initialize[A]) =
    Def.settingDyn(if (githubEnabled.value) f else Def.setting(default))

  @SuppressWarnings(Array("scalafix:Disable.Option.get"))
  private def onRepo[A](default: A)(f: Def.Initialize[Repository => A]) =
    Def.settingDyn {
      if (githubEnabled.value) Def.setting(f.value(repository.value.get)) else Def.setting(default)
    }

}
