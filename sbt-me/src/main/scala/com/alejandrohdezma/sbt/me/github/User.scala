package com.alejandrohdezma.sbt.me.github

import com.alejandrohdezma.sbt.me.json.Decoder
import com.alejandrohdezma.sbt.me.syntax.json._

final case class User(
    login: String,
    url: String,
    name: Option[String],
    email: Option[String],
    avatar: Option[String]
) {

  /** Returns this user as a Github organization */
  def asOrganization: Organization = Organization(name, Some(url), email)

}

object User {

  implicit val decoder: Decoder[User] = json =>
    for {
      login  <- json.get[String]("login")
      url    <- json.get[String]("html_url")
      name   <- json.get[Option[String]]("name")
      email  <- json.get[Option[String]]("email")
      avatar <- json.get[Option[String]]("avatar_url")
    } yield User(
      login,
      url,
      name.filter(_.nonEmpty),
      email.filter(_.nonEmpty),
      avatar.filter(_.nonEmpty)
    )

}
