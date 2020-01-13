package com.alejandrohdezma.sbt.me.github

import com.alejandrohdezma.sbt.me.json.Decoder
import com.alejandrohdezma.sbt.me.syntax.json._

/** Represents a repository collaborator */
final case class Collaborator(
    login: String,
    url: String,
    userUrl: String,
    name: Option[String],
    email: Option[String],
    avatar: Option[String]
)

object Collaborator {

  implicit val CollaboratorDecoder: Decoder[Collaborator] = json =>
    for {
      login   <- json.get[String]("login")
      url     <- json.get[String]("html_url")
      userUrl <- json.get[String]("url")
      avatar  <- json.get[Option[String]]("avatar_url")
    } yield Collaborator(login, url, userUrl, None, None, avatar.filter(_.nonEmpty))

}
