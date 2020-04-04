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

package com.alejandrohdezma.sbt.github.github.urls

import sbt.util.Logger

import com.alejandrohdezma.sbt.github.github.error.GithubError
import com.alejandrohdezma.sbt.github.http.{client, Authentication}
import com.alejandrohdezma.sbt.github.json.Decoder
import com.alejandrohdezma.sbt.github.syntax.json._
import com.alejandrohdezma.sbt.github.syntax.scalatry._

final case class UserEntryPoint(base: String) {

  def get(login: String): String = base.replace("{user}", login)

}

object UserEntryPoint {

  @SuppressWarnings(Array("scalafix:Disable.get"))
  implicit def user(
      implicit auth: Authentication,
      logger: Logger,
      entryPoint: GithubEntryPoint
  ): UserEntryPoint =
    client
      .get[UserEntryPoint](entryPoint.value)
      .failAs(GithubError("Unable to connect to Github"))
      .get

  implicit val UserUrlDecoder: Decoder[UserEntryPoint] = json =>
    json.get[String]("user_url").map(UserEntryPoint(_))

}
