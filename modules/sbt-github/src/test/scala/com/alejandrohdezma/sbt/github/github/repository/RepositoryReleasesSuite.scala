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

import java.time.ZonedDateTime

import scala.util.Failure
import scala.util.Success

import com.alejandrohdezma.sbt.github._
import com.alejandrohdezma.sbt.github.github._
import com.alejandrohdezma.sbt.github.github.error.GithubError
import org.http4s.dsl.io._

class RepositoryReleasesSuite extends munit.FunSuite {

  test("repository.releases should return ordered list from Github API") {
    withServer { case GET -> Root / "releases" =>
      Ok(s"""[
          {
            "tag_name": "v3.0.0",
            "name": "v3.0.0"
          },
          {
            "tag_name": "v2.1.0",
            "name": "v2.1.0",
            "published_at": "2021-07-30T15:22:45Z"
          },
          {
            "tag_name": "v2.0.0",
            "name": "v2.0.0",
            "published_at": "2021-07-20T20:52:19Z"
          },
          {
            "tag_name": "v1.0.0",
            "name": "v1.0.0",
            "published_at": "2021-07-08T18:43:09Z"
          }
        ]""")
    } { uri =>
      val repository = EmptyRepository.copy(releasesUrl = url"${uri}releases")

      val releases = repository.releases

      val expected = List(
        Release("v1.0.0", "v1.0.0", Some(ZonedDateTime.parse("2021-07-08T18:43:09Z"))),
        Release("v2.0.0", "v2.0.0", Some(ZonedDateTime.parse("2021-07-20T20:52:19Z"))),
        Release("v2.1.0", "v2.1.0", Some(ZonedDateTime.parse("2021-07-30T15:22:45Z"))),
        Release("v3.0.0", "v3.0.0", None)
      )

      assertEquals(releases, Success(expected))
    }

    test("repository.releases should return generic error on any error") {
      withServer { case GET -> Root / "releases" =>
        Ok("""{"hello": "hi"}""")
      } { uri =>
        val repository = EmptyRepository.copy(releasesUrl = url"${uri}releases")

        val releases = repository.releases

        val expected = GithubError("Unable to get repository releases")

        assertEquals(releases, Failure(expected))
      }
    }

    test("Repository.isPublished should return true if publishDate is present") {
      val publishedRelease = Release("", "", Some(ZonedDateTime.now()))

      assertEquals(publishedRelease.isPublished, true)
    }

    test("Repository.isPublished should return false if publishDate is missing") {
      val publishedRelease = Release("", "", None)

      assertEquals(publishedRelease.isPublished, false)
    }

  }

}
