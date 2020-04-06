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

import scala.util.Try

import sbt.util.Logger

import com.alejandrohdezma.sbt.github.github.urls.{GithubEntryPoint, UserEntryPoint}
import com.alejandrohdezma.sbt.github.http.{client, Authentication}
import com.alejandrohdezma.sbt.github.json.Decoder
import com.alejandrohdezma.sbt.github.syntax.json._

/** Represents a repository collaborator */
final case class Collaborator private[github] (
    login: String,
    url: String,
    userUrl: Option[String],
    name: Option[String],
    email: Option[String],
    avatar: Option[String]
)

object Collaborator {

  type Creator = Authentication => GithubEntryPoint => Logger => Try[Collaborator]

  /**
   * Obtains a collaborator information from its Github login ID
   */
  def github(id: String): Collaborator.Creator = {
    implicit auth => implicit entrypoint => implicit log =>
      for {
        _    <- Try(log.info(s"Retrieving `$id` information from Github API"))
        url  <- UserEntryPoint.get(id)
        user <- client.get[User](url)
      } yield Collaborator(user.login, user.url, None, user.name, user.email, user.avatar)
  }

  /**
   * Creates a new collaborator
   *
   * @param login the Github login ID for the collaborator
   * @param name the collaborator's full name
   * @param url the collaborator's URL. It may link to its Github profile or personal webpage.
   * @return a new collaborator
   */
  def apply(login: String, name: String, url: String): Collaborator.Creator =
    _ => _ => _ => Try(new Collaborator(login, url, None, Some(name), None, None))

  /**
   * Creates a new collaborator
   *
   * @param login the Github login ID for the collaborator
   * @param name the collaborator's full name
   * @param url the collaborator's URL. It may link to its Github profile or personal webpage.
   * @param email the collaborator's email
   * @return a new collaborator
   */
  def apply(login: String, name: String, url: String, email: String): Collaborator.Creator =
    _ => _ => _ => Try(new Collaborator(login, url, None, Some(name), Some(email), None))

  /**
   * Creates a new collaborator
   *
   * @param login the Github login ID for the collaborator
   * @param name the collaborator's full name
   * @param url the collaborator's URL. It may link to its Github profile or personal webpage.
   * @param email the collaborator's email, optional
   * @param avatar the collaborator's avatar URL, optional
   * @return a new collaborator
   */
  def apply(
      login: String,
      name: String,
      url: String,
      email: Option[String],
      avatar: Option[String]
  ): Collaborator.Creator =
    _ => _ => _ => Try(new Collaborator(login, url, None, Some(name), email, avatar))

  implicit val CollaboratorDecoder: Decoder[Collaborator] = json =>
    for {
      login   <- json.get[String]("login")
      url     <- json.get[String]("html_url")
      userUrl <- json.get[String]("url")
      avatar  <- json.get[Option[String]]("avatar_url")
    } yield Collaborator(login, url, Some(userUrl), None, None, avatar.filter(_.nonEmpty))

}
