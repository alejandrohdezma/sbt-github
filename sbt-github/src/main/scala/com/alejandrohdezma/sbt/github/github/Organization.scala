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

import sbt.URL

import com.alejandrohdezma.sbt.github.json.Decoder
import com.alejandrohdezma.sbt.github.syntax.json._

/** Represents a repository's organization */
final case class Organization(name: Option[String], url: Option[URL], email: Option[String])

object Organization {

  implicit val OrganizationDecoder: Decoder[Organization] = json =>
    for {
      name  <- json.get[Option[String]]("name")
      url   <- json.get[Option[URL]]("blog")
      email <- json.get[Option[String]]("email")
    } yield Organization(name, url, email)

}