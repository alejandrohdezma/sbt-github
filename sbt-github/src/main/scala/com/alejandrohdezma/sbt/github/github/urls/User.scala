package com.alejandrohdezma.sbt.github.github.urls

import sbt.util.Logger

import com.alejandrohdezma.sbt.github.http.{client, Authentication}
import com.alejandrohdezma.sbt.github.json.Decoder
import com.alejandrohdezma.sbt.github.syntax.json._

final case class User(base: String) {

  def get(login: String): String = base.replace("{user}", login)

}

object User {

  implicit def user(
      implicit auth: Authentication,
      logger: Logger,
      entryPoint: GithubEntryPoint
  ): User =
    client
      .get[User](entryPoint.value)
      .getOrElse(sys.error("Unable to connect to Github"))

  implicit val UserUrlDecoder: Decoder[User] = json => json.get[String]("user_url").map(User(_))

}
