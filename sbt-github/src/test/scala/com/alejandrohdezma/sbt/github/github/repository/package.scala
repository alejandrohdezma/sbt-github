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

import sbt.util.Logger

import com.alejandrohdezma.sbt.github._
import com.alejandrohdezma.sbt.github.http.Authentication
import com.alejandrohdezma.sbt.github.http.Authentication.Token

package object repository {

  implicit val authentication: Authentication = Token("1234")
  implicit val noOpLogger: Logger             = Logger.Null

  lazy val EmptyRepository: Repository =
    Repository(
      "",
      "",
      License("", url"http://example.com"),
      url"http://example.com",
      0,
      url"http://example.com",
      url"http://example.com",
      None,
      url"http://example.com"
    )

}
