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

import sbt._

import com.alejandrohdezma.sbt.github.github.Organization
import com.alejandrohdezma.sbt.github.github.Repository

@SuppressWarnings(Array("scalafix:DisableSyntax.valInAbstract"))
trait SbtGithubKeys {

  type Contributors = github.Contributors
  val Contributors = github.Contributors

  type Collaborators = github.Collaborators
  val Collaborators = github.Collaborators

  type Collaborator = github.Collaborator
  val Collaborator = github.Collaborator

  @deprecated("Use AuthToken instead", since = "0.8.1")
  type Token = http.Authentication.Token

  @deprecated("Use AuthToken instead", since = "0.8.1")
  val Token = http.Authentication.Token

  type AuthToken = http.Authentication.AuthToken
  val AuthToken = http.Authentication.AuthToken

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

  val githubEnabled = settingKey[Boolean] {
    "Whether sbt-github should download information from Github or not. Default to `false`"
  }

  @deprecated("Use githubEnabled instead", since = "0.8.0")
  val downloadInfoFromGithub = settingKey[Boolean] {
    "Whether sbt-github should download information from Github or not. Defaults to the presence of" +
      " a `DOWNLOAD_INFO_FROM_GITHUB` environment variable. Deprecated, use `githubEnabled` instead."
  }

  val yearRange = settingKey[Option[String]] {
    "Range of years in which the project has been active"
  }

  val githubOrganization = settingKey[String] {
    "The ID of the Github organization to be used when populating `organizationName`, `organizationEmail` " +
      "and `organizationHomepage`. If no organization is provided, those settings will be populated with the " +
      "information of the repository's organization."
  }

  val organizationEmail = settingKey[Option[String]] {
    "Organization email"
  }

  @deprecated("Use githubAuthToken instead", since = "0.8.1")
  val githubToken = settingKey[Token] {
    "The Github Token used for authenticating into Github API. Defaults to GITHUB_TOKEN environment variable. " +
      "Deprecated, use `githubAuthToken` instead."
  }

  val githubAuthToken = settingKey[Option[AuthToken]] {
    "The Github Token used for authenticating into Github API. Defaults to GITHUB_TOKEN environment variable."
  }

}
