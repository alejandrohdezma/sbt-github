package com.alejandrohdezma.sbt.me.github

import java.time.ZonedDateTime

import sbt.URL

import com.alejandrohdezma.sbt.me.http.{client, Authentication}
import com.alejandrohdezma.sbt.me.json.Decoder
import com.alejandrohdezma.sbt.me.json.Json.Fail.NotFound
import com.alejandrohdezma.sbt.me.syntax.either._
import com.alejandrohdezma.sbt.me.syntax.json._

/** Represents a repository in Github */
final case class Repository(
    description: String,
    license: License,
    url: String,
    startYear: Int,
    contributorsUrl: String
) {

  /** Returns the license extracted from github in the format that SBT is expecting */
  def licenses: List[(String, URL)] = List(license.id -> sbt.url(license.url))

  /**
   * Returns the list of users who have contributed to a repository order by the number
   * of contributions.
   *
   * Excludes from the list those whose login ID appears in the provided list of
   * excluded contributors.
   */
  def contributors(
      excluded: List[String]
  )(implicit auth: Authentication): Either[String, Contributors] = {
    client
      .get[List[Contributor]](contributorsUrl)
      .map(_.sortBy(-_.contributions))
      .map(_.filterNot(contributor => excluded.contains(contributor.login)))
      .map(Contributors)
      .leftMap(_ => "Unable to get repository contributors")
  }

}

object Repository {

  /** Download repository information from Github, or returns a string containing the error */
  def get(user: String, name: String)(
      implicit auth: Authentication,
      url: urls.Repository
  ): Either[String, Repository] =
    client.get[Repository](url.get(user, name)).leftMap {
      case "description" / NotFound =>
        s"Repository doesn't have a description! Go to https://github.com/$user/$name and add it"
      case "license" / NotFound =>
        s"Repository doesn't have a license! Go to https://github.com/$user/$name and add it"
      case "license" / ("spdx_id" / NotFound) =>
        s"Repository's license id couldn't be inferred! Go to https://github.com/$user/$name and check it"
      case "license" / ("url" / NotFound) =>
        s"Repository's license url couldn't be inferred! Go to https://github.com/$user/$name and check it"
      case _ => "Unable to get repository information"
    }

  implicit val RepositoryDecoder: Decoder[Repository] = json =>
    for {
      description     <- json.get[String]("description")
      license         <- json.get[License]("license")
      url             <- json.get[String]("html_url")
      startYear       <- json.get[ZonedDateTime]("created_at")
      contributorsUrl <- json.get[String]("contributors_url")
    } yield Repository(description, license, url, startYear.getYear, contributorsUrl)

}
