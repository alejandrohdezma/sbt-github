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

package com.alejandrohdezma.sbt.github.github.repository

import scala.util.Failure
import scala.util.Success

import com.alejandrohdezma.sbt.github._
import com.alejandrohdezma.sbt.github.github._
import com.alejandrohdezma.sbt.github.github.error.GithubError
import org.http4s.dsl.io._

class RepositoryOwnerSuite extends munit.FunSuite {

  test("repository.owner should return repository's owner from Github API") {
    withServer { case GET -> Root / "owner" =>
      Ok("""{
          "login": "owner",
          "html_url": "http://example.com/owner",
          "name": "Owner",
          "email": "owner@example.com",
          "avatar_url": "http://example.com/owner.png"
        }""")
    } { uri =>
      val repository = EmptyRepository.copy(ownerUrl = url"${uri}owner")

      val owner = repository.owner

      val expected = User(
        "owner",
        url"http://example.com/owner",
        Some("Owner"),
        Some("owner@example.com"),
        Some(url"http://example.com/owner.png")
      )

      assertEquals(owner, Success(expected))
    }
  }

  test("repository.owner should not return name if not present") {
    withServer { case GET -> Root / "owner" =>
      Ok("""{
          "login": "owner",
          "html_url": "http://example.com/owner",
          "email": "owner@example.com"
        }""")
    } { uri =>
      val repository = EmptyRepository.copy(ownerUrl = url"${uri}owner")

      val owner = repository.owner

      val expected =
        User("owner", url"http://example.com/owner", None, Some("owner@example.com"), None)

      assertEquals(owner, Success(expected))
    }
  }

  test("repository.owner should not return email if not present") {
    withServer { case GET -> Root / "owner" =>
      Ok("""{
          "login": "owner",
          "html_url": "http://example.com/owner",
          "name": "Owner"
        }""")
    } { uri =>
      val repository = EmptyRepository.copy(ownerUrl = url"${uri}owner")

      val owner = repository.owner

      val expected = User("owner", url"http://example.com/owner", Some("Owner"), None, None)

      assertEquals(owner, Success(expected))
    }
  }

  test("repository.owner should not return avatar if not present") {
    withServer { case GET -> Root / "owner" =>
      Ok("""{
          "login": "owner",
          "html_url": "http://example.com/owner",
          "name": "Owner",
          "email": "owner@example.com"
        }""")
    } { uri =>
      val repository = EmptyRepository.copy(ownerUrl = url"${uri}owner")

      val owner = repository.owner

      val expected = User(
        "owner",
        url"http://example.com/owner",
        Some("Owner"),
        Some("owner@example.com"),
        None
      )

      assertEquals(owner, Success(expected))
    }
  }

  test("repository.owner should return generic error on any error") {
    withServer { case GET -> Root / "owner" =>
      NotFound()
    } { uri =>
      val repository = EmptyRepository.copy(ownerUrl = url"${uri}owner")

      val owner = repository.owner

      val expected = GithubError("Unable to get repository owner")

      assertEquals(owner, Failure(expected))
    }
  }

}
