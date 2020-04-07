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

package com.alejandrohdezma.sbt.github.http

import java.io.IOException

import scala.util.Failure

import cats.implicits._

import sbt.util.Logger

import com.alejandrohdezma.sbt.github._
import com.alejandrohdezma.sbt.github.http.error.URLNotFound
import com.alejandrohdezma.sbt.github.json.Decoder
import com.alejandrohdezma.sbt.github.syntax.json.JsonValueOps
import org.http4s.dsl.io._
import org.http4s.headers.Authorization
import org.specs2.mutable.Specification

@SuppressWarnings(Array("scalafix:Disable.IOException"))
class ClientSpec extends Specification {

  "client.get" should {

    "make `GET` call using auth and returning content as `A`" >> withServer {
      case req @ GET -> Root / "hello" =>
        Ok(s"""{
          "auth": "${req.headers.get(Authorization).map(_.renderString).orEmpty}"
        }""")
    } { uri =>
      final case class Auth(auth: String)

      implicit val decoder: Decoder[Auth] = _.get[String]("auth").map(Auth)
      implicit val auth: Authentication   = Authentication.Token("1234")

      val result = client.get[Auth](url"${uri}hello")

      result must beSuccessfulTry(Auth("Authorization: token 1234"))
    }

    "returns NotFound for unreachable urls" >> withServer {
      case GET -> Root / "hello" => NotFound()
    } { uri =>
      implicit val auth: Authentication = Authentication.Token("1234")

      val result = client.get[String](url"${uri}hello")

      result must beAFailedTry(equalTo(URLNotFound(url"${uri}hello")))
    }

    "propagate for every other failure (http-related)" >> withServer {
      case GET -> Root => Forbidden()
    } { url =>
      implicit val auth: Authentication = Authentication.Token("1234")

      val result = client.get[String](url)

      result must be like {
        case Failure(e: IOException) =>
          e.getMessage must be equalTo s"Server returned HTTP response code: 403 for URL: $url"
      }
    }

  }

  implicit val noOpLogger: Logger = Logger.Null

}
