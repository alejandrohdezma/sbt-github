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

import sbt.Def.Setting
import sbt.Keys._
import sbt._
import sbt.plugins.JvmPlugin

import com.alejandrohdezma.sbt.github.github.Organization
import com.alejandrohdezma.sbt.github.github.Repository
import com.alejandrohdezma.sbt.github.github.urls.GithubEntryPoint
import com.alejandrohdezma.sbt.github.http.Authentication
import com.alejandrohdezma.sbt.github.syntax.list._

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
@SuppressWarnings(Array("scalafix:DisableSyntax.=="))
object SbtGithubPlugin extends AutoPlugin {

  object autoImport {

    type Contributors = github.Contributors
    val Contributors = github.Contributors

    type Collaborators = github.Collaborators
    val Collaborators = github.Collaborators

    type Collaborator = github.Collaborator
    val Collaborator = github.Collaborator

    type Token = http.Authentication.Token
    val Token = http.Authentication.Token

    val githubApiEntryPoint = settingKey[URL] {
      "Entry point for the github API, defaults to `https://api.github.com`"
    }

    val contributors = settingKey[Contributors](
      "List of contributors downloaded from Github"
    )

    val collaborators = settingKey[Collaborators](
      "List of collaborators downloaded from Github"
    )

    val organizationMetadata = settingKey[Option[Organization]] {
      "Organization information downloaded from Github"
    }

    val populateOrganizationWithOwner = settingKey[Boolean] {
      "Populate organization info with the owner one in case there is no organization, default to `true`"
    }

    val extraCollaborators = settingKey[List[Collaborator.Creator]] {
      "Extra collaborators that should be always included (independent of whether they are contributors or not)"
    }

    val excludedContributors = settingKey[List[String]] {
      "ID (Github login) of the contributors that should be excluded from the list, like bots, it can also be regex patterns"
    }

    val repository = settingKey[Option[Repository]] {
      "Repository information downloaded from Github"
    }

    val downloadInfoFromGithub = settingKey[Boolean] {
      "Whether sbt-github should download information from Github or not"
    }

    val yearRange = settingKey[Option[String]] {
      "Range of years in which the project has been active"
    }

    val organizationEmail = settingKey[Option[String]] {
      "Organization email"
    }

    val githubToken = settingKey[Token] {
      "The Github Token used for authenticating into Github API. Defaults to GITHUB_TOKEN environment variable."
    }

  }

  import autoImport._

  override def trigger: PluginTrigger = allRequirements

  override def requires: Plugins = JvmPlugin

  override def buildSettings: Seq[Setting[_]] = Seq(
    githubApiEntryPoint           := url("https://api.github.com"),
    downloadInfoFromGithub        := sys.env.contains("DOWNLOAD_INFO_FROM_GITHUB"),
    populateOrganizationWithOwner := true,
    excludedContributors          := List("scala-steward", """.*\[bot\]""", "traviscibot"),
    extraCollaborators            := List(),
    githubToken := Token {
      sys.env.getOrElse("GITHUB_TOKEN", sys.error {
        "You need to add an environment variable named GITHUB_TOKEN with a Github personal access token."
      })
    },
    repository := Def.settingDyn {
      if (downloadInfoFromGithub.value) Def.setting {
        implicit val log: Logger                  = sLog.value
        implicit val entryPoint: GithubEntryPoint = GithubEntryPoint(githubApiEntryPoint.value)
        implicit val auth: Authentication         = githubToken.value

        Some(Repository.get(info.value._1, info.value._2).get)
      }
      else Def.setting(None)
    }.value,
    organizationMetadata := {
      implicit val log: Logger          = sLog.value
      implicit val auth: Authentication = githubToken.value
      repository.value
        .flatMap(_.organization)
        .orElse {
          repository.value
            .filter(_ => populateOrganizationWithOwner.value)
            .map(_.owner.map(_.asOrganization))
        }
        .map(_.get)
    },
    contributors := {
      implicit val log: Logger          = sLog.value
      implicit val auth: Authentication = githubToken.value
      repository.value.fold(Contributors(Nil)) {
        _.contributors(excludedContributors.value).get
      }
    },
    collaborators := {
      implicit val log: Logger          = sLog.value
      implicit val auth: Authentication = githubToken.value
      repository.value.fold(Collaborators(Nil)) {
        _.collaborators(contributors.value.list.map(_.login)).get
          .include(
            extraCollaborators.value
              .map(_(auth)(GithubEntryPoint(githubApiEntryPoint.value))(log))
              .traverse(identity)
              .get
          )
      }
    },
    developers := collaborators.value.developers,
    homepage   := repository.value.map(_.url).orElse(homepage.value),
    licenses   := repository.value.map(_.licenses).getOrElse(licenses.value),
    startYear  := repository.value.map(_.startYear).orElse(startYear.value),
    yearRange := startYear.value.collect {
      case start if start == Year.now.getValue => s"$start"
      case start                               => s"$start-${Year.now.getValue}"
    }
  )

  override def projectSettings: Seq[Def.Setting[_]] = Seq(
    description := repository.value.map(_.description).getOrElse(description.value),
    organizationName := organizationMetadata.value
      .flatMap(_.name)
      .getOrElse(organizationName.value),
    organizationHomepage := organizationMetadata.value.fold(organizationHomepage.value)(_.url),
    organizationEmail    := organizationMetadata.value.flatMap(_.email)
  )

  /** Gets the Github user and repository from the git remote info */
  private[github] val info = Def.setting {
    val identifier = """([^\/]+)"""

    val Connection = s"scm:git:https://github.com/$identifier/$identifier.git".r

    scmInfo.value.map(_.connection) match {
      case Some(Connection(owner, repo)) => (owner, repo)
      case None                          => sys.error("`scmInfo` is mandatory for this plugin to work")
      case Some(s)                       => sys.error(s"Invalid `scmInfo` connection value: $s")
    }
  }

}
