package com.alejandrohdezma.sbt.github.github

import sbt.util.Logger

import com.alejandrohdezma.sbt.github._
import com.alejandrohdezma.sbt.github.http.Authentication
import com.alejandrohdezma.sbt.github.http.Authentication.Token

package object repository {

  implicit val authentication: Authentication = Token("1234")
  implicit val noOpLogger: Logger             = Logger.Null

  lazy val EmptyRepository: Repository =
    Repository(
      "",
      "",
      License("", url"http://example.com"),
      url"http://example.com",
      0,
      url"http://example.com",
      url"http://example.com",
      None,
      url"http://example.com"
    )

}
