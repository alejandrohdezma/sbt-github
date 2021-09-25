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

package com.alejandrohdezma.sbt.github.github.repository

import scala.util.Failure
import scala.util.Success

import com.alejandrohdezma.sbt.github._
import com.alejandrohdezma.sbt.github.github._
import com.alejandrohdezma.sbt.github.github.error.GithubError
import com.alejandrohdezma.sbt.github.github.urls.GithubEntryPoint
import org.http4s.dsl.io._

class RepositorySuite extends munit.FunSuite {

  test("Repository.get should return Repository if everything is present") {
    withServer {
      case r @ GET -> Root => Ok(s"""{ "repository_url": "${r.urlTo("{owner}/{repo}")}" } """)
      case GET -> Root / "user" / "repo" =>
        Ok("""{
            "full_name": "user/repo",
            "description": "The description",
            "html_url": "http://example.com/repository",
            "created_at": "2011-01-26T19:01:12Z",
            "contributors_url": "http://api.github.com/repos/example/example/contributors",
            "collaborators_url": "http://api.github.com/repos/example/example/collaborators",
            "releases_url": "http://api.github.com/repos/example/example/releases",
            "organization": {
              "url": "http://api.github.com/users/example"
            },
            "owner": {
              "url": "http://api.github.com/users/owner"
            },
            "license": {
              "spdx_id": "id",
              "url": "http://example.com"
            }
          }""")
    } { url =>
      implicit val entryPoint: GithubEntryPoint = GithubEntryPoint(url)

      val repository = Repository.get("user", "repo")

      val expected = Repository(
        "user/repo",
        "The description",
        License("id", url"http://example.com"),
        url"http://example.com/repository",
        2011,
        url"http://api.github.com/repos/example/example/contributors",
        url"http://api.github.com/repos/example/example/collaborators",
        url"http://api.github.com/repos/example/example/releases",
        Some(url"http://api.github.com/users/example"),
        url"http://api.github.com/users/owner"
      )

      assertEquals(repository, Success(expected))
    }
  }

  test("Repository.get should return None organization if it is not present") {
    withServer {
      case r @ GET -> Root => Ok(s"""{ "repository_url": "${r.urlTo("{owner}/{repo}")}" } """)
      case GET -> Root / "user" / "repo" =>
        Ok("""{
            "full_name": "user/repo",
            "description": "The description",
            "html_url": "http://example.com/repository",
            "created_at": "2011-01-26T19:01:12Z",
            "contributors_url": "http://api.github.com/repos/example/example/contributors",
            "collaborators_url": "http://api.github.com/repos/example/example/collaborators",
            "releases_url": "http://api.github.com/repos/example/example/releases",
            "owner": {
              "url": "http://api.github.com/users/owner"
            },
            "license": {
              "spdx_id": "id",
              "url": "http://example.com"
            }
          }""")
    } { url =>
      implicit val entryPoint: GithubEntryPoint = GithubEntryPoint(url)

      val repository = Repository.get("user", "repo")

      val expected = Repository(
        "user/repo",
        "The description",
        License("id", url"http://example.com"),
        url"http://example.com/repository",
        2011,
        url"http://api.github.com/repos/example/example/contributors",
        url"http://api.github.com/repos/example/example/collaborators",
        url"http://api.github.com/repos/example/example/releases",
        None,
        url"http://api.github.com/users/owner"
      )

      assertEquals(repository, Success(expected))
    }
  }

  test("Repository.get should return error if description is not present") {
    withServer {
      case r @ GET -> Root => Ok(s"""{ "repository_url": "${r.urlTo("{owner}/{repo}")}" } """)
      case GET -> Root / "user" / "repo" =>
        Ok("""{
            "full_name": "user/repo",
            "description": null,
            "html_url": "http://example.com/repository",
            "created_at": "2011-01-26T19:01:12Z",
            "contributors_url": "http://api.github.com/repos/example/example/contributors",
            "collaborators_url": "http://api.github.com/repos/example/example/collaborators",
            "owner": {
              "url": "http://api.github.com/users/owner"
            },
            "organization": {
              "url": "http://api.github.com/users/example"
            },
            "license": {
              "spdx_id": "id",
              "url": "http://example.com"
            }
          }""")
    } { url =>
      implicit val entryPoint: GithubEntryPoint = GithubEntryPoint(url)

      val repository = Repository.get("user", "repo")

      val expected = GithubError(
        "Repository doesn't have a description! Go to https://github.com/user/repo and add it"
      )

      assertEquals(repository, Failure(expected))
    }
  }

  test("Repository.get should return error if license is not present") {
    withServer {
      case r @ GET -> Root => Ok(s"""{ "repository_url": "${r.urlTo("{owner}/{repo}")}" } """)
      case GET -> Root / "user" / "repo" =>
        Ok("""{
            "full_name": "user/repo",
            "description": "The description",
            "html_url": "http://example.com/repository",
            "created_at": "2011-01-26T19:01:12Z",
            "contributors_url": "http://api.github.com/repos/example/example/contributors",
            "collaborators_url": "http://api.github.com/repos/example/example/collaborators",
            "owner": {
              "url": "http://api.github.com/users/owner"
            },
            "organization": {
              "url": "http://api.github.com/users/example"
            },
            "license": null
          }""")
    } { url =>
      implicit val entryPoint: GithubEntryPoint = GithubEntryPoint(url)

      val repository = Repository.get("user", "repo")

      val expected = GithubError(
        "Repository doesn't have a license! Go to https://github.com/user/repo and add it"
      )

      assertEquals(repository, Failure(expected))
    }
  }

  test("Repository.get should return error if license's `spdx_id` is not present") {
    withServer {
      case r @ GET -> Root => Ok(s"""{ "repository_url": "${r.urlTo("{owner}/{repo}")}" } """)
      case GET -> Root / "user" / "repo" =>
        Ok("""{
            "full_name": "user/repo",
            "description": "The description",
            "html_url": "http://example.com/repository",
            "created_at": "2011-01-26T19:01:12Z",
            "contributors_url": "http://api.github.com/repos/example/example/contributors",
            "collaborators_url": "http://api.github.com/repos/example/example/collaborators",
            "owner": {
              "url": "http://api.github.com/users/owner"
            },
            "organization": {
              "url": "http://api.github.com/users/example"
            },
            "license": {
              "spdx_id": null,
              "url": "http://example.com"
            }
          }""")
    } { url =>
      implicit val entryPoint: GithubEntryPoint = GithubEntryPoint(url)

      val repository = Repository.get("user", "repo")

      val expected = GithubError(
        "Repository's license id couldn't be inferred! Go to https://github.com/user/repo and check it"
      )

      assertEquals(repository, Failure(expected))
    }
  }

  test("Repository.get should return error if license's `url` is not present") {
    withServer {
      case r @ GET -> Root => Ok(s"""{ "repository_url": "${r.urlTo("{owner}/{repo}")}" } """)
      case GET -> Root / "user" / "repo" =>
        Ok("""{
            "full_name": "user/repo",
            "description": "The description",
            "html_url": "http://example.com/repository",
            "created_at": "2011-01-26T19:01:12Z",
            "contributors_url": "http://api.github.com/repos/example/example/contributors",
            "collaborators_url": "http://api.github.com/repos/example/example/collaborators",
            "owner": {
              "url": "http://api.github.com/users/owner"
            },
            "organization": {
              "url": "http://api.github.com/users/example"
            },
            "license": {
              "spdx_id": "id",
              "url": null
            }
          }""")
    } { url =>
      implicit val entryPoint: GithubEntryPoint = GithubEntryPoint(url)

      val repository = Repository.get("user", "repo")

      val expected = GithubError(
        "Repository's license url couldn't be inferred! Go to https://github.com/user/repo and check it"
      )

      assertEquals(repository, Failure(expected))
    }
  }

  test("Repository.get should return generic error in other cases") {
    withServer {
      case r @ GET -> Root => Ok(s"""{ "repository_url": "${r.urlTo("{owner}/{repo}")}" } """)
      case GET -> Root / "user" / "repo" =>
        Ok("""{
            "full_name": "user/repo",
            "description": "The description",
            "html_url": "http://example.com/repository",
            "created_at": "2011-01-26T19:01:12Z",
            "contributors_url": "http://api.github.com/repos/example/example/contributors",
            "collaborators_url": "http://api.github.com/repos/example/example/collaborators",
            "owner": {
              "url": "http://api.github.com/users/owner"
            },
            "organization": {
              "url": "http://api.github.com/users/example"
            },
            "license": 42
          }""")
    } { url =>
      implicit val entryPoint: GithubEntryPoint = GithubEntryPoint(url)

      val repository = Repository.get("user", "repo")

      val expected = GithubError(
        "Unable to get repository information"
      )

      assertEquals(repository, Failure(expected))
    }
  }

}
