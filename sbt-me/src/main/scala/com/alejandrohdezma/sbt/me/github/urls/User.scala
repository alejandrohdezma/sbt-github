package com.alejandrohdezma.sbt.me.github.urls

import sbt.util.Logger

import com.alejandrohdezma.sbt.me.http.{client, Authentication}
import com.alejandrohdezma.sbt.me.json.Decoder
import com.alejandrohdezma.sbt.me.syntax.json._

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
