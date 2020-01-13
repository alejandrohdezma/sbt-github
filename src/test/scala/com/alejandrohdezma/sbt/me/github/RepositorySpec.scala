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
        "http://example.com/repository"
      )

      repository must beRight(expected)
    }

    "return error if description is not present" >> withServer {
      case GET -> Root / "user" / "repo" =>
        Ok("""{
            "description": null,
            "html_url": "http://example.com/repository",
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
            "license": 42
          }""")
    } { uri =>
      implicit val url: urls.Repository = urls.Repository(s"$uri{owner}/{repo}")

      val repository = Repository.get("user", "repo")

      repository must beLeft("Unable to get repository information")
    }

  }

}
