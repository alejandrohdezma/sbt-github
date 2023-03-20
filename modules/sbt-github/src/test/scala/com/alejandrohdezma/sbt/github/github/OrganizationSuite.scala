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

package com.alejandrohdezma.sbt.github.github

import scala.util.Failure
import scala.util.Success

import sbt.util.Logger

import com.alejandrohdezma.sbt.github._
import com.alejandrohdezma.sbt.github.github.error.GithubError
import com.alejandrohdezma.sbt.github.github.urls.GithubEntryPoint
import com.alejandrohdezma.sbt.github.http.Authentication
import com.alejandrohdezma.sbt.github.http.Authentication.AuthToken
import org.http4s.dsl.io._

class OrganizationSuite extends munit.FunSuite {

  test("Organization.get should return Organization if everything is present") {
    withServer {
      case r @ GET -> Root => Ok(s"""{ "organization_url": "${r.urlTo("{org}")}" } """)
      case GET -> Root / "org" =>
        Ok("""{
          "name": "My Organization",
          "blog": "http://example.com",
          "email": "org@example.com"
        }""")
    } { url =>
      implicit val entryPoint: GithubEntryPoint   = GithubEntryPoint(url)
      implicit val authentication: Authentication = AuthToken("1234")
      implicit val noOpLogger: Logger             = Logger.Null

      val organization = Organization.get("org")

      val expected = Organization(
        Some("My Organization"),
        Some(url"http://example.com"),
        Some("org@example.com")
      )

      assertEquals(organization, Success(expected))
    }
  }

  test("Repository.get should return error if fail to obtain organization") {
    withServer {
      case r @ GET -> Root               => Ok(s"""{ "repository_url": "${r.urlTo("{owner}/{repo}")}" } """)
      case GET -> Root / "user" / "repo" => NotFound()
    } { url =>
      implicit val entryPoint: GithubEntryPoint   = GithubEntryPoint(url)
      implicit val authentication: Authentication = AuthToken("1234")
      implicit val noOpLogger: Logger             = Logger.Null

      val organization = Organization.get("org")

      val expected = GithubError("Unable to get `org` organization")

      assertEquals(organization, Failure(expected))
    }
  }

}
