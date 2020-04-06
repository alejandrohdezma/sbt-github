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

package com.alejandrohdezma.sbt.github.github

import sbt.util.Logger

import com.alejandrohdezma.sbt.github._
import com.alejandrohdezma.sbt.github.github.error.GithubError
import com.alejandrohdezma.sbt.github.github.urls.GithubEntryPoint
import com.alejandrohdezma.sbt.github.http.Authentication
import com.alejandrohdezma.sbt.github.http.Authentication.Token
import org.http4s.dsl.io._
import org.specs2.mutable.Specification

class RepositorySpec extends Specification {

  "Repository.get" should {

    "return Repository if everything is present" >> withServer {
      case r @ GET -> Root => Ok(s"""{ "repository_url": "${r.urlTo("{owner}/{repo}")}" } """)
      case GET -> Root / "user" / "repo" =>
        Ok("""{
            "full_name": "user/repo",
            "description": "The description",
            "html_url": "http://example.com/repository",
            "created_at": "2011-01-26T19:01:12Z",
            "contributors_url": "http://api.github.com/repos/example/example/contributors",
            "collaborators_url": "http://api.github.com/repos/example/example/collaborators",
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
    } { uri =>
      implicit val entryPoint: GithubEntryPoint = GithubEntryPoint(sbt.url(uri))

      val repository = Repository.get("user", "repo")

      val expected = Repository(
        "user/repo",
        "The description",
        License("id", sbt.url("http://example.com")),
        "http://example.com/repository",
        2011,
        "http://api.github.com/repos/example/example/contributors",
        "http://api.github.com/repos/example/example/collaborators",
        Some("http://api.github.com/users/example"),
        "http://api.github.com/users/owner"
      )

      repository must beSuccessfulTry(expected)
    }

    "return None organization if it is not present" >> withServer {
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
            "license": {
              "spdx_id": "id",
              "url": "http://example.com"
            }
          }""")
    } { uri =>
      implicit val entryPoint: GithubEntryPoint = GithubEntryPoint(sbt.url(uri))

      val repository = Repository.get("user", "repo")

      val expected = Repository(
        "user/repo",
        "The description",
        License("id", sbt.url("http://example.com")),
        "http://example.com/repository",
        2011,
        "http://api.github.com/repos/example/example/contributors",
        "http://api.github.com/repos/example/example/collaborators",
        None,
        "http://api.github.com/users/owner"
      )

      repository must beSuccessfulTry(expected)
    }

    "return error if description is not present" >> withServer {
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
    } { uri =>
      implicit val entryPoint: GithubEntryPoint = GithubEntryPoint(sbt.url(uri))

      val repository = Repository.get("user", "repo")

      val expected = GithubError(
        "Repository doesn't have a description! Go to https://github.com/user/repo and add it"
      )

      repository must beAFailedTry(equalTo(expected))
    }

    "return error if license is not present" >> withServer {
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
    } { uri =>
      implicit val entryPoint: GithubEntryPoint = GithubEntryPoint(sbt.url(uri))

      val repository = Repository.get("user", "repo")

      val expected = GithubError(
        "Repository doesn't have a license! Go to https://github.com/user/repo and add it"
      )

      repository must beAFailedTry(equalTo(expected))
    }

    "return error if license's `spdx_id` is not present" >> withServer {
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
    } { uri =>
      implicit val entryPoint: GithubEntryPoint = GithubEntryPoint(sbt.url(uri))

      val repository = Repository.get("user", "repo")

      val expected = GithubError(
        "Repository's license id couldn't be inferred! Go to https://github.com/user/repo and check it"
      )

      repository must beAFailedTry(equalTo(expected))
    }

    "return error if license's `url` is not present" >> withServer {
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
    } { uri =>
      implicit val entryPoint: GithubEntryPoint = GithubEntryPoint(sbt.url(uri))

      val repository = Repository.get("user", "repo")

      val expected = GithubError(
        "Repository's license url couldn't be inferred! Go to https://github.com/user/repo and check it"
      )

      repository must beAFailedTry(equalTo(expected))
    }

    "return generic error in other cases" >> withServer {
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
    } { uri =>
      implicit val entryPoint: GithubEntryPoint = GithubEntryPoint(sbt.url(uri))

      val repository = Repository.get("user", "repo")

      val expected = GithubError(
        "Unable to get repository information"
      )

      repository must beAFailedTry(equalTo(expected))
    }

  }

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
      val repository = EmptyRepository.copy(contributorsUrl = s"${uri}contributors")

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
      val repository = EmptyRepository.copy(contributorsUrl = s"${uri}contributors")

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
      val repository = EmptyRepository.copy(contributorsUrl = s"${uri}contributors")

      val contributors = repository.contributors(List(""".*\[bot\]"""))

      val expected = Contributors(List(Contributor("me", 42, "http://example.com/me", None)))

      contributors must beSuccessfulTry(expected)
    }

    "return generic error on any error" >> withServer {
      case GET -> Root / "contributors" => Ok("""{"hello": "hi"}""")
    } { uri =>
      val repository = EmptyRepository.copy(contributorsUrl = s"${uri}contributors")

      val contributors = repository.contributors(Nil)

      val expected = GithubError(
        "Unable to get repository contributors"
      )

      contributors must beAFailedTry(equalTo(expected))
    }

  }

  "repository.collaborators" should {

    "return list of collaborators (alphabetically ordered) with user info from Github API" >> withServer {
      case GET -> Root / "me"  => Ok("""{
        "name": "Me",
        "login": "me",
        "html_url": "http://example.com/me",
        "email": "me@example.com"
      }""")
      case GET -> Root / "you" => Ok("""{
        "login": "you",
        "html_url": "http://example.com/you",
        "avatar_url": "http://example.com/you.png"
      }""")
      case GET -> Root / "him" => Ok("""{
        "name": "Him",
        "login": "him",
        "html_url": "http://example.com/him"
      }""")
      case req @ GET -> Root / "collaborators" =>
        Ok(s"""[
          {
            "login": "me",
            "avatar_url": "http://example.com/me.png",
            "html_url": "http://example.com/me",
            "url": "${req.urlTo("me")}"
          },
          {
            "login": "you",
            "html_url": "http://example.com/you",
            "url": "${req.urlTo("you")}"
          },
          {
            "login": "him",
            "avatar_url": null,
            "html_url": "http://example.com/him",
            "url": "${req.urlTo("him")}"
          }
        ]""")
    } { uri =>
      val repository = EmptyRepository.copy(collaboratorsUrl = s"${uri}collaborators")

      val collaborators = repository.collaborators(List("me", "you", "him"))

      val expected = Collaborators(
        Collaborator(
          "you",
          sbt.url("http://example.com/you"),
          Some(sbt.url(s"${uri}you")),
          None,
          None,
          None
        ),
        Collaborator(
          "him",
          sbt.url("http://example.com/him"),
          Some(sbt.url(s"${uri}him")),
          Some("Him"),
          None,
          None
        ),
        Collaborator(
          "me",
          sbt.url("http://example.com/me"),
          Some(sbt.url(s"${uri}me")),
          Some("Me"),
          Some("me@example.com"),
          Some(sbt.url("http://example.com/me.png"))
        )
      )

      collaborators must beSuccessfulTry(expected)
    }

    "exclude collaborators not in provided list and don't retrieve user info for them" >> withServer {
      case GET -> Root / "me" =>
        Ok("""{"login": "me", "html_url": "http://example.com/me", "name": "Me"}""")
      case req @ GET -> Root / "collaborators" =>
        Ok(s"""[
          {
            "login": "me",
            "avatar_url": "http://example.com/me.png",
            "html_url": "http://example.com/me",
            "url": "${req.urlTo("me")}"
          },
          {
            "login": "you",
            "html_url": "http://example.com/you",
            "url": "http://api.example.com/you"
          }
        ]""")
    } { uri =>
      val repository = EmptyRepository.copy(collaboratorsUrl = s"${uri}collaborators")

      val collaborators = repository.collaborators(List("me"))

      val expected = Collaborators(
        Collaborator(
          "me",
          sbt.url("http://example.com/me"),
          Some(sbt.url(s"${uri}me")),
          Some("Me"),
          None,
          Some(sbt.url("http://example.com/me.png"))
        )
      )

      collaborators must beSuccessfulTry(expected)
    }

    "return generic error on any error" >> withServer {
      case GET -> Root / "collaborators" => Ok("""{"hello": "hi"}""")
    } { uri =>
      val repository = EmptyRepository.copy(collaboratorsUrl = s"${uri}collaborators")

      val collaborators = repository.collaborators(Nil)

      val expected = GithubError("Unable to get repository collaborators")

      collaborators must beAFailedTry(equalTo(expected))
    }

  }

  "repository.organization" should {

    "return repository's organization from Github API" >> withServer {
      case GET -> Root / "organization" =>
        Ok(s"""{
          "name": "My Organization",
          "blog": "http://example.com",
          "email": "org@example.com"
        }""")
    } { uri =>
      val repository = EmptyRepository.copy(organizationUrl = Some(s"${uri}organization"))

      val organization = repository.organization

      val expected =
        Organization(
          Some("My Organization"),
          Some(sbt.url("http://example.com")),
          Some("org@example.com")
        )

      organization must be some successfulTry(expected)
    }

    "not return url if not present" >> withServer {
      case GET -> Root / "organization" =>
        Ok(s"""{ "name": "My Organization", "email": "org@example.com" }""")
    } { uri =>
      val repository = EmptyRepository.copy(organizationUrl = Some(s"${uri}organization"))

      val organization = repository.organization

      val expected = Organization(Some("My Organization"), None, Some("org@example.com"))

      organization must be some successfulTry(expected)
    }

    "not return name if not present" >> withServer {
      case GET -> Root / "organization" =>
        Ok(s"""{ "blog": "http://example.com", "email": "org@example.com" }""")
    } { uri =>
      val repository = EmptyRepository.copy(organizationUrl = Some(s"${uri}organization"))

      val organization = repository.organization

      val expected =
        Organization(None, Some(sbt.url("http://example.com")), Some("org@example.com"))

      organization must be some successfulTry(expected)
    }

    "not return email if not present" >> withServer {
      case GET -> Root / "organization" =>
        Ok(s"""{ "blog": "http://example.com", "name": "My Organization" }""")
    } { uri =>
      val repository = EmptyRepository.copy(organizationUrl = Some(s"${uri}organization"))

      val organization = repository.organization

      val expected =
        Organization(Some("My Organization"), Some(sbt.url("http://example.com")), None)

      organization must be some successfulTry(expected)
    }

    "return generic error on any error" >> withServer {
      case GET -> Root / "organization" => NotFound()
    } { uri =>
      val repository = EmptyRepository.copy(organizationUrl = Some(s"${uri}organization"))

      val organization = repository.organization

      val expected = GithubError("Unable to get repository organization")

      organization must beSome(failedTry[Organization](equalTo(expected)))
    }

  }

  "repository.owner" should {

    "return repository's owner from Github API" >> withServer {
      case GET -> Root / "owner" =>
        Ok(s"""{
          "login": "owner",
          "html_url": "http://example.com/owner",
          "name": "Owner",
          "email": "owner@example.com",
          "avatar_url": "http://example.com/owner.png"
        }""")
    } { uri =>
      val repository = EmptyRepository.copy(ownerUrl = s"${uri}owner")

      val owner = repository.owner

      val expected = User(
        "owner",
        sbt.url("http://example.com/owner"),
        Some("Owner"),
        Some("owner@example.com"),
        Some(sbt.url("http://example.com/owner.png"))
      )

      owner must beSuccessfulTry(expected)
    }

    "not return name if not present" >> withServer {
      case GET -> Root / "owner" =>
        Ok(s"""{
          "login": "owner",
          "html_url": "http://example.com/owner",
          "email": "owner@example.com"
        }""")
    } { uri =>
      val repository = EmptyRepository.copy(ownerUrl = s"${uri}owner")

      val owner = repository.owner

      val expected =
        User("owner", sbt.url("http://example.com/owner"), None, Some("owner@example.com"), None)

      owner must beSuccessfulTry(expected)
    }

    "not return email if not present" >> withServer {
      case GET -> Root / "owner" =>
        Ok(s"""{
          "login": "owner",
          "html_url": "http://example.com/owner",
          "name": "Owner"
        }""")
    } { uri =>
      val repository = EmptyRepository.copy(ownerUrl = s"${uri}owner")

      val owner = repository.owner

      val expected = User("owner", sbt.url("http://example.com/owner"), Some("Owner"), None, None)

      owner must beSuccessfulTry(expected)
    }

    "not return avatar if not present" >> withServer {
      case GET -> Root / "owner" =>
        Ok(s"""{
          "login": "owner",
          "html_url": "http://example.com/owner",
          "name": "Owner",
          "email": "owner@example.com"
        }""")
    } { uri =>
      val repository = EmptyRepository.copy(ownerUrl = s"${uri}owner")

      val owner = repository.owner

      val expected = User(
        "owner",
        sbt.url("http://example.com/owner"),
        Some("Owner"),
        Some("owner@example.com"),
        None
      )

      owner must beSuccessfulTry(expected)
    }

    "return generic error on any error" >> withServer {
      case GET -> Root / "owner" => NotFound()
    } { uri =>
      val repository = EmptyRepository.copy(ownerUrl = s"${uri}owner")

      val owner = repository.owner

      val expected = GithubError("Unable to get repository owner")

      owner must beAFailedTry(equalTo(expected))
    }

  }

  implicit val authentication: Authentication = Token("1234")
  implicit val noOpLogger: Logger             = Logger.Null

  lazy val EmptyRepository: Repository =
    Repository("", "", License("", sbt.url("http://example.com")), "", 0, "", "", None, "")

}
