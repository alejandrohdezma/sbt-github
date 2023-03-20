/*
 * Copyright 2019-2023 Alejandro Hernández <https://github.com/alejandrohdezma>
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

import scala.util.Try

import sbt.URL
import sbt.util.Logger

import com.alejandrohdezma.sbt.github.github.error.GithubError
import com.alejandrohdezma.sbt.github.http._
import com.alejandrohdezma.sbt.github.syntax.json._
import com.alejandrohdezma.sbt.github.syntax.scalatry._

object OrganizationEntryPoint {

  /** Returns the entry point URL for a given organization. */
  def get(organization: String)(implicit
      auth: Authentication,
      logger: Logger,
      entryPoint: GithubEntryPoint
  ): Try[URL] =
    client
      .get[String](entryPoint.value)(_.get[String]("organization_url"), auth, logger)
      .failAs(GithubError("Unable to connect to Github"))
      .map(_.replace("{org}", organization))
      .map(sbt.url)

}
