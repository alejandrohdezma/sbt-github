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

package com.alejandrohdezma.sbt.github.github.urls

import scala.util.Success

import sbt.util.Logger

import com.alejandrohdezma.sbt.github._
import com.alejandrohdezma.sbt.github.http.Authentication
import com.alejandrohdezma.sbt.github.http.Authentication.AuthToken
import org.http4s.dsl.io._

class OrganizationEntryPointSuite extends munit.FunSuite {

  test("OrganizationEntryPoint.get should provide url for specific organization") {
    withServer { case GET -> Root =>
      Ok("""{ "organization_url": "http://example.com/{org}" }""")
    } { url =>
      implicit val noOpLogger: Logger                 = Logger.Null
      implicit val githubEntryPoint: GithubEntryPoint = GithubEntryPoint(url)
      implicit val auth: Authentication               = AuthToken("1234")

      val organizationUrl = OrganizationEntryPoint.get("org")

      assertEquals(organizationUrl, Success(url"http://example.com/org"))
    }
  }

}
