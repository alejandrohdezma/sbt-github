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

package com.alejandrohdezma.sbt.github.github

import java.time.ZonedDateTime

import scala.util.Try

import sbt.URL
import sbt.util.Logger

import com.alejandrohdezma.sbt.github.error.NotFound
import com.alejandrohdezma.sbt.github.github.error.GithubError
import com.alejandrohdezma.sbt.github.github.urls.{GithubEntryPoint, RepositoryEntryPoint}
import com.alejandrohdezma.sbt.github.http.{client, Authentication}
import com.alejandrohdezma.sbt.github.json.Decoder
import com.alejandrohdezma.sbt.github.syntax.json._
import com.alejandrohdezma.sbt.github.syntax.list._
import com.alejandrohdezma.sbt.github.syntax.scalatry._

/** Represents a repository in Github */
final case class Repository(
    name: String,
    description: String,
    license: License,
    url: String,
    startYear: Int,
    contributorsUrl: String,
    collaboratorsUrl: String,
    organizationUrl: Option[String],
    ownerUrl: String
) {

  /** Returns the license extracted from github in the format that SBT is expecting */
  def licenses: List[(String, URL)] = List(license.id -> sbt.url(license.url))

  /**
   * Returns the list of users who have contributed to a repository order by the number
   * of contributions.
   *
   * Excludes from the list those whose login ID matches any in the provided list of
   * excluded contributors patterns.
   */
  def contributors(
      excluded: List[String]
  )(implicit auth: Authentication, logger: Logger): Try[Contributors] = {
    logger.info(s"Retrieving `$name` contributors from Github API")

    client
      .get[List[Contributor]](contributorsUrl)
      .map(_.sortBy(-_.contributions))
      .map(_.filterNot(contributor => excluded.exists(contributor.login.matches)))
      .map(Contributors)
      .failAs(GithubError("Unable to get repository contributors"))
  }

  /**
   * Returns the list of repository collaborators, filtered by those who have contributed
   * at least once to the project, alphabetically ordered.
   */
  def collaborators(allowed: List[String])(
      implicit auth: Authentication,
      logger: Logger
  ): Try[Collaborators] = {
    logger.info(s"Retrieving `$name` collaborators from Github API")

    client
      .get[List[Collaborator]](collaboratorsUrl)
      .map(_.filter(m => allowed.contains(m.login)))
      .flatMap(_.traverse { collaborator =>
        logger.info(s"Retrieving `${collaborator.login}` information from Github API")

        collaborator.userUrl.map {
          client.get[User](_).map { user =>
            collaborator.copy(name = user.name, email = user.email)
          }
        }.getOrElse(Try(collaborator))
      })
      .map(_.sortBy(collaborator => collaborator.name -> collaborator.login))
      .map(Collaborators(_))
      .failAs(GithubError("Unable to get repository collaborators"))
  }

  /**
   * Returns the repository's organization information, if present.
   */
  def organization(
      implicit auth: Authentication,
      logger: Logger
  ): Option[Try[Organization]] = {
    logger.info(s"Retrieving `$name` organization from Github API")

    organizationUrl
      .map(client.get[Organization])
      .map(_.failAs(GithubError("Unable to get repository organization")))
  }

  /**
   * Returns the repository's owner information.
   */
  def owner(implicit auth: Authentication, logger: Logger): Try[User] = {
    logger.info(s"Retrieving `$name` owner from Github API")

    client.get[User](ownerUrl).failAs(GithubError("Unable to get repository owner"))
  }

}

object Repository {

  /** Download repository information from Github, or returns a string containing the error */
  def get(owner: String, name: String)(
      implicit auth: Authentication,
      logger: Logger,
      url: GithubEntryPoint
  ): Try[Repository] = RepositoryEntryPoint.get(owner, name).flatMap { uri =>
    logger.info(s"Retrieving `$owner/$name` information from Github API")

    val descriptionNotFound =
      s"Repository doesn't have a description! Go to https://github.com/$owner/$name and add it"
    val licenseNotFound =
      s"Repository doesn't have a license! Go to https://github.com/$owner/$name and add it"
    val licenseNotInferred =
      s"Repository's license id couldn't be inferred! Go to https://github.com/$owner/$name and check it"
    val urlNotInferred =
      s"Repository's license url couldn't be inferred! Go to https://github.com/$owner/$name and check it"

    client
      .get[Repository](uri)
      .collectFail {
        case "description" / NotFound           => GithubError(descriptionNotFound)
        case "license" / NotFound               => GithubError(licenseNotFound)
        case "license" / ("spdx_id" / NotFound) => GithubError(licenseNotInferred)
        case "license" / ("url" / NotFound)     => GithubError(urlNotInferred)
        case _                                  => GithubError("Unable to get repository information")
      }

  }

  implicit val RepositoryDecoder: Decoder[Repository] = json =>
    for {
      name            <- json.get[String]("full_name")
      description     <- json.get[String]("description")
      license         <- json.get[License]("license")
      url             <- json.get[String]("html_url")
      startYear       <- json.get[ZonedDateTime]("created_at")
      contributors    <- json.get[String]("contributors_url")
      collaborators   <- json.get[String]("collaborators_url")
      organizationUrl <- json.get[Option[String]]("organization", "url")
      ownerUrl        <- json.get[String]("owner", "url")
    } yield Repository(
      name,
      description,
      license,
      url,
      startYear.getYear,
      contributors,
      collaborators.replace("{/collaborator}", ""),
      organizationUrl,
      ownerUrl
    )

}
