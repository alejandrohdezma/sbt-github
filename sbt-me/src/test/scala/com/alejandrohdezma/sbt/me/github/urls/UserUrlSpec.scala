package com.alejandrohdezma.sbt.me.github.urls

import sbt.util.Logger

import com.alejandrohdezma.sbt.me.http.Authentication
import com.alejandrohdezma.sbt.me.http.Authentication.Token
import com.alejandrohdezma.sbt.me.withServer
import org.http4s.dsl.io._
import org.specs2.mutable.Specification

class UserUrlSpec extends Specification {

  "User" should {

    "provide implicit value based on others" >> withServer {
      case GET -> Root =>
        Ok("""{ "user_url": "miau" }""")
    } { uri =>
      implicit val noOpLogger: Logger                 = Logger.Null
      implicit val githubEntryPoint: GithubEntryPoint = GithubEntryPoint(uri)
      implicit val auth: Authentication               = Token("1234")

      val repoUrl = implicitly[User]

      val expected = User("miau")

      repoUrl must be equalTo expected
    }

    "provide url for specific user" >> withServer {
      case GET -> Root =>
        Ok("""{ "user_url": "http://example.com/{user}" }""")
    } { uri =>
      implicit val noOpLogger: Logger                 = Logger.Null
      implicit val githubEntryPoint: GithubEntryPoint = GithubEntryPoint(uri)
      implicit val auth: Authentication               = Token("1234")

      val repoUrl = implicitly[User]

      repoUrl.get("user") must be equalTo "http://example.com/user"
    }

  }

}
