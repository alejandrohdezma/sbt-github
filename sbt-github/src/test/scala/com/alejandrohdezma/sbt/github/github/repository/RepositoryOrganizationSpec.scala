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

class RepositoryOrganizationSpec extends Specification {

  "repository.organization" should {

    "return repository's organization from Github API" >> withServer {
      case GET -> Root / "organization" =>
        Ok(s"""{
          "name": "My Organization",
          "blog": "http://example.com",
          "email": "org@example.com"
        }""")
    } { uri =>
      val repository = EmptyRepository.copy(organizationUrl = Some(url"${uri}organization"))

      val organization = repository.organization

      val expected =
        Organization(
          Some("My Organization"),
          Some(url"http://example.com"),
          Some("org@example.com")
        )

      organization must be some successfulTry(expected)
    }

    "not return url if not present" >> withServer {
      case GET -> Root / "organization" =>
        Ok(s"""{ "name": "My Organization", "email": "org@example.com" }""")
    } { uri =>
      val repository = EmptyRepository.copy(organizationUrl = Some(url"${uri}organization"))

      val organization = repository.organization

      val expected = Organization(Some("My Organization"), None, Some("org@example.com"))

      organization must be some successfulTry(expected)
    }

    "not return name if not present" >> withServer {
      case GET -> Root / "organization" =>
        Ok(s"""{ "blog": "http://example.com", "email": "org@example.com" }""")
    } { uri =>
      val repository = EmptyRepository.copy(organizationUrl = Some(url"${uri}organization"))

      val organization = repository.organization

      val expected = Organization(None, Some(url"http://example.com"), Some("org@example.com"))

      organization must be some successfulTry(expected)
    }

    "not return email if not present" >> withServer {
      case GET -> Root / "organization" =>
        Ok(s"""{ "blog": "http://example.com", "name": "My Organization" }""")
    } { uri =>
      val repository = EmptyRepository.copy(organizationUrl = Some(url"${uri}organization"))

      val organization = repository.organization

      val expected = Organization(Some("My Organization"), Some(url"http://example.com"), None)

      organization must be some successfulTry(expected)
    }

    "return generic error on any error" >> withServer {
      case GET -> Root / "organization" => NotFound()
    } { uri =>
      val repository = EmptyRepository.copy(organizationUrl = Some(url"${uri}organization"))

      val organization = repository.organization

      val expected = GithubError("Unable to get repository organization")

      organization must beSome(failedTry[Organization](equalTo(expected)))
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
