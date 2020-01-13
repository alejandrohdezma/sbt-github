package com.alejandrohdezma.sbt.me.github

import com.alejandrohdezma.sbt.me.withServer
import org.http4s.dsl.io._
import org.specs2.mutable.Specification

class RepositorySpec extends Specification {

  "Repository.get" should {

    "return Repository if everything is present" >> withServer {
      case GET -> Root / "user" / "repo" =>
        Ok("""{
            "description": "The description",
            "html_url": "http://example.com/repository",
            "created_at": "2011-01-26T19:01:12Z",
            "contributors_url": "http://api.github.com/repos/example/example/contributors",
            "collaborators_url": "http://api.github.com/repos/example/example/collaborators",
            "license": {
              "spdx_id": "id",
              "url": "http://example.com"
            }
          }""")
    } { uri =>
      implicit val url: urls.Repository = urls.Repository(s"$uri{owner}/{repo}")

      val repository = Repository.get("user", "repo")

      val expected = Repository(
        "The description",
        License("id", "http://example.com"),
        "http://example.com/repository",
        2011,
        "http://api.github.com/repos/example/example/contributors",
        "http://api.github.com/repos/example/example/collaborators"
      )

      repository must beRight(expected)
    }

    "return error if description is not present" >> withServer {
      case GET -> Root / "user" / "repo" =>
        Ok("""{
            "description": null,
            "html_url": "http://example.com/repository",
            "created_at": "2011-01-26T19:01:12Z",
            "contributors_url": "http://api.github.com/repos/example/example/contributors",
            "collaborators_url": "http://api.github.com/repos/example/example/collaborators",
            "license": {
              "spdx_id": "id",
              "url": "http://example.com"
            }
          }""")
    } { uri =>
      implicit val url: urls.Repository = urls.Repository(s"$uri{owner}/{repo}")

      val repository = Repository.get("user", "repo")

      repository must beLeft {
        "Repository doesn't have a description! Go to https://github.com/user/repo and add it"
      }
    }

    "return error if license is not present" >> withServer {
      case GET -> Root / "user" / "repo" =>
        Ok("""{
            "description": "The description",
            "html_url": "http://example.com/repository",
            "created_at": "2011-01-26T19:01:12Z",
            "contributors_url": "http://api.github.com/repos/example/example/contributors",
            "collaborators_url": "http://api.github.com/repos/example/example/collaborators",
            "license": null
          }""")
    } { uri =>
      implicit val url: urls.Repository = urls.Repository(s"$uri{owner}/{repo}")

      val repository = Repository.get("user", "repo")

      repository must beLeft {
        "Repository doesn't have a license! Go to https://github.com/user/repo and add it"
      }
    }

    "return error if license's `ipdx_id` is not present" >> withServer {
      case GET -> Root / "user" / "repo" =>
        Ok("""{
            "description": "The description",
            "html_url": "http://example.com/repository",
            "created_at": "2011-01-26T19:01:12Z",
            "contributors_url": "http://api.github.com/repos/example/example/contributors",
            "collaborators_url": "http://api.github.com/repos/example/example/collaborators",
            "license": {
              "spdx_id": null,
              "url": "http://example.com"
            }
          }""")
    } { uri =>
      implicit val url: urls.Repository = urls.Repository(s"$uri{owner}/{repo}")

      val repository = Repository.get("user", "repo")

      repository must beLeft {
        "Repository's license id couldn't be inferred! Go to https://github.com/user/repo and check it"
      }
    }

    "return error if license's `ipdx_id` is not present" >> withServer {
      case GET -> Root / "user" / "repo" =>
        Ok("""{
            "description": "The description",
            "html_url": "http://example.com/repository",
            "created_at": "2011-01-26T19:01:12Z",
            "contributors_url": "http://api.github.com/repos/example/example/contributors",
            "collaborators_url": "http://api.github.com/repos/example/example/collaborators",
            "license": {
              "spdx_id": "id",
              "url": null
            }
          }""")
    } { uri =>
      implicit val url: urls.Repository = urls.Repository(s"$uri{owner}/{repo}")

      val repository = Repository.get("user", "repo")

      repository must beLeft {
        "Repository's license url couldn't be inferred! Go to https://github.com/user/repo and check it"
      }
    }

    "return generic error in other cases" >> withServer {
      case GET -> Root / "user" / "repo" =>
        Ok("""{
            "description": "The description",
            "html_url": "http://example.com/repository",
            "created_at": "2011-01-26T19:01:12Z",
            "contributors_url": "http://api.github.com/repos/example/example/contributors",
            "collaborators_url": "http://api.github.com/repos/example/example/collaborators",
            "license": 42
          }""")
    } { uri =>
      implicit val url: urls.Repository = urls.Repository(s"$uri{owner}/{repo}")

      val repository = Repository.get("user", "repo")

      repository must beLeft("Unable to get repository information")
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
      val repository = Repository("", License("", ""), "", 0, s"${uri}contributors", "")

      val contributors = repository.contributors(Nil)

      val expected = Contributors(
        List(
          Contributor("me", 42, "http://example.com/me", Some("http://example.com/me.png")),
          Contributor("him", 6, "http://example.com/him", None),
          Contributor("you", 2, "http://example.com/you", None)
        )
      )

      contributors must beRight(expected)
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
      val repository = Repository("", License("", ""), "", 0, s"${uri}contributors", "")

      val contributors = repository.contributors(List("you"))

      val expected = Contributors(List(Contributor("me", 42, "http://example.com/me", None)))

      contributors must beRight(expected)
    }

    "return generic error on any error" >> withServer {
      case GET -> Root / "contributors" => Ok("""{"hello": "hi"}""")
    } { uri =>
      val repository = Repository("", License("", ""), "", 0, s"${uri}contributors", "")

      val contributors = repository.contributors(Nil)

      contributors must beLeft("Unable to get repository contributors")
    }

  }

  "repository.collaborators" should {

    "return list of collaborators (alphabetically ordered) from Github API" >> withServer {
      case GET -> Root / "collaborators" =>
        Ok("""[
          {
            "login": "me",
            "avatar_url": "http://example.com/me.png",
            "html_url": "http://example.com/me"
          },
          {
            "login": "you",
            "html_url": "http://example.com/you"
          },
          {
            "login": "him",
            "avatar_url": null,
            "html_url": "http://example.com/him"
          }
        ]""")
    } { uri =>
      val repository = Repository("", License("", ""), "", 0, "", s"${uri}collaborators")

      val collaborators = repository.collaborators(List("me", "you", "him"))

      val expected = Collaborators(
        List(
          Collaborator("him", "http://example.com/him", None),
          Collaborator("me", "http://example.com/me", Some("http://example.com/me.png")),
          Collaborator("you", "http://example.com/you", None)
        )
      )

      collaborators must beRight(expected)
    }

    "exclude collaborators not in provided list" >> withServer {
      case GET -> Root / "collaborators" =>
        Ok("""[
          {
            "login": "me",
            "avatar_url": "http://example.com/me.png",
            "html_url": "http://example.com/me"
          },
          {
            "login": "you",
            "html_url": "http://example.com/you"
          }
        ]""")
    } { uri =>
      val repository = Repository("", License("", ""), "", 0, "", s"${uri}collaborators")

      val collaborators = repository.collaborators(List("me"))

      val expected = Collaborators(
        List(Collaborator("me", "http://example.com/me", Some("http://example.com/me.png")))
      )

      collaborators must beRight(expected)
    }

    "return generic error on any error" >> withServer {
      case GET -> Root / "collaborators" => Ok("""{"hello": "hi"}""")
    } { uri =>
      val repository = Repository("", License("", ""), "", 0, "", s"${uri}collaborators")

      val collaborators = repository.collaborators(Nil)

      collaborators must beLeft("Unable to get repository collaborators")
    }

  }

}
