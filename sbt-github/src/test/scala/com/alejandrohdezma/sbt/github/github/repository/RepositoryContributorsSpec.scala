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

package com.alejandrohdezma.sbt.github.github.repository

import sbt.util.Logger

import com.alejandrohdezma.sbt.github._
import com.alejandrohdezma.sbt.github.github._
import com.alejandrohdezma.sbt.github.github.error.GithubError
import com.alejandrohdezma.sbt.github.http.Authentication
import com.alejandrohdezma.sbt.github.http.Authentication.Token
import org.http4s.dsl.io._
import org.specs2.mutable.Specification

class RepositoryContributorsSpec extends Specification {

  "repository.contributors" should {

    "return list of contributors (ordered by contributions) from Github API" >> withServer {
      case GET -> Root / "contributors" =>
        Ok("""[
          {
            "login": "me",
            "avatar_url": "http://example.com/me.png",
            "html_url": "http://example.com/me",
            "contributions": 42
          },
          {
            "login": "you",
            "html_url": "http://example.com/you",
            "contributions": 2
          },
          {
            "login": "him",
            "avatar_url": null,
            "html_url": "http://example.com/him",
            "contributions": 6
          }
        ]
        """)
    } { uri =>
      val repository = EmptyRepository.copy(contributorsUrl = url"${uri}contributors")

      val contributors = repository.contributors(Nil)

      val expected = Contributors(
        List(
          Contributor("me", 42, "http://example.com/me", Some("http://example.com/me.png")),
          Contributor("him", 6, "http://example.com/him", None),
          Contributor("you", 2, "http://example.com/you", None)
        )
      )

      contributors must beSuccessfulTry(expected)
    }

    "exclude provided contributors" >> withServer {
      case GET -> Root / "contributors" =>
        Ok("""[
          {
            "login": "me",
            "html_url": "http://example.com/me",
            "contributions": 42
          },
          {
            "login": "you",
            "html_url": "http://example.com/you",
            "contributions": 2
          }
        ]
        """)
    } { uri =>
      val repository = EmptyRepository.copy(contributorsUrl = url"${uri}contributors")

      val contributors = repository.contributors(List("you"))

      val expected = Contributors(List(Contributor("me", 42, "http://example.com/me", None)))

      contributors must beSuccessfulTry(expected)
    }

    "exclude provided contributors as regex" >> withServer {
      case GET -> Root / "contributors" =>
        Ok("""[
          {
            "login": "me",
            "html_url": "http://example.com/me",
            "contributions": 42
          },
          {
            "login": "you[bot]",
            "html_url": "http://example.com/you",
            "contributions": 2
          }
        ]
        """)
    } { uri =>
      val repository = EmptyRepository.copy(contributorsUrl = url"${uri}contributors")

      val contributors = repository.contributors(List(""".*\[bot\]"""))

      val expected = Contributors(List(Contributor("me", 42, "http://example.com/me", None)))

      contributors must beSuccessfulTry(expected)
    }

    "return generic error on any error" >> withServer {
      case GET -> Root / "contributors" => Ok("""{"hello": "hi"}""")
    } { uri =>
      val repository = EmptyRepository.copy(contributorsUrl = url"${uri}contributors")

      val contributors = repository.contributors(Nil)

      val expected = GithubError(
        "Unable to get repository contributors"
      )

      contributors must beAFailedTry(equalTo(expected))
    }

  }

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
