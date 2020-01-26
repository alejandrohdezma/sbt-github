package com.alejandrohdezma.sbt.github.github.urls

import sbt.util.Logger

import com.alejandrohdezma.sbt.github.http.Authentication
import com.alejandrohdezma.sbt.github.http.Authentication.Token
import com.alejandrohdezma.sbt.github.withServer
import org.http4s.dsl.io._
import org.specs2.mutable.Specification

class RepositoryUrlSpec extends Specification {

  "Repository" should {

    "provide implicit value based on others" >> withServer {
      case GET -> Root =>
        Ok("""{ "repository_url": "miau" }""")
    } { uri =>
      implicit val noOpLogger: Logger                 = Logger.Null
      implicit val githubEntryPoint: GithubEntryPoint = GithubEntryPoint(uri)
      implicit val auth: Authentication               = Token("1234")

      val repoUrl = implicitly[Repository]

      val expected = Repository("miau")

      repoUrl must be equalTo expected
    }

    "provide url for specific repository" >> withServer {
      case GET -> Root =>
        Ok("""{ "repository_url": "http://example.com/{owner}/{repo}" }""")
    } { uri =>
      implicit val noOpLogger: Logger                 = Logger.Null
      implicit val githubEntryPoint: GithubEntryPoint = GithubEntryPoint(uri)
      implicit val auth: Authentication               = Token("1234")

      val repoUrl = implicitly[Repository]

      repoUrl.get("owner", "repo") must be equalTo "http://example.com/owner/repo"
    }

  }

}
