/*
 * Copyright 2019-2021 Alejandro Hernández <https://github.com/alejandrohdezma>
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

import com.alejandrohdezma.sbt.github.json.Decoder
import com.alejandrohdezma.sbt.github.syntax.json._

/** Represents a repository release */
final case class Release(name: String, tag: String, publishDate: Option[ZonedDateTime]) {

  lazy val isPublished: Boolean = publishDate.nonEmpty

}

object Release {

  implicit val ReleaseDecoder: Decoder[Release] = json =>
    for {
      name        <- json.get[String]("name")
      tag         <- json.get[String]("tag_name")
      publishDate <- json.get[Option[ZonedDateTime]]("published_at")
    } yield Release(name, tag, publishDate)

}
