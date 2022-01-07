/*
 * Copyright 2019-2022 Alejandro Hernández <https://github.com/alejandrohdezma>
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

import sbt.URL

import com.alejandrohdezma.sbt.github.json.Decoder
import com.alejandrohdezma.sbt.github.syntax.json._

final case class User(
    login: String,
    url: URL,
    name: Option[String],
    email: Option[String],
    avatar: Option[URL]
) {

  /** Returns this user as a Github organization */
  def asOrganization: Organization = Organization(name, Some(url), email)

}

object User {

  implicit val decoder: Decoder[User] = json =>
    for {
      login  <- json.get[String]("login")
      url    <- json.get[URL]("html_url")
      name   <- json.get[Option[String]]("name")
      email  <- json.get[Option[String]]("email")
      avatar <- json.get[Option[URL]]("avatar_url")
    } yield User(login, url, name.filter(_.nonEmpty), email.filter(_.nonEmpty), avatar)

}
