/*
 * Copyright 2019-2024 Alejandro Hernández <https://github.com/alejandrohdezma>
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

package com.alejandrohdezma.sbt.github.http

import java.io.FileNotFoundException
import java.util.concurrent.ConcurrentHashMap

import scala.io.Source
import scala.util.Try

import sbt.URL
import sbt.util.Logger

import com.alejandrohdezma.sbt.github.http.error.URLNotFound
import com.alejandrohdezma.sbt.github.json.Decoder
import com.alejandrohdezma.sbt.github.json.Json
import com.alejandrohdezma.sbt.github.syntax.json._
import com.alejandrohdezma.sbt.github.syntax.scalatry._

object client {

  /** Calls the provided URL with the provided authentication, downloads its contents as JSON and transforms and returns
    * it using the provided `Decoder`.
    */
  @SuppressWarnings(Array("all"))
  def get[A: Decoder](url: URL)(implicit auth: Authentication, logger: Logger): Try[A] =
    Try {
      logger.verbose(s"Getting content from URL: $url")

      if (cache.containsKey(url))
        logger.verbose(s"$url contents already stored on cache")

      cache.computeIfAbsent(
        url,
        { _ =>
          logger.verbose(s"Content for $url not found on cache, downloading...")

          val connection = url.openConnection

          connection.setRequestProperty("Authorization", auth.header)

          val inputStream = connection.getInputStream

          Source.fromInputStream(inputStream, "UTF-8").mkString
        }
      )
    }.collectFail { case _: FileNotFoundException =>
      URLNotFound(url)
    }.flatMap(Json.parse).as[A]

  private val cache: ConcurrentHashMap[URL, String] = new ConcurrentHashMap[URL, String]()

}
