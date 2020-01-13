package com.alejandrohdezma.sbt.me.github

import com.alejandrohdezma.sbt.me.json.Decoder
import com.alejandrohdezma.sbt.me.syntax.json._

/** Represents a repository contributor */
final case class Contributor(
    login: String,
    contributions: Int,
    url: String,
    avatar: Option[String]
)

object Contributor {

  implicit val ContributorDecoder: Decoder[Contributor] = json =>
    for {
      login         <- json.get[String]("login")
      contributions <- json.get[Int]("contributions")
      url           <- json.get[String]("html_url")
      avatar        <- json.get[Option[String]]("avatar_url")
    } yield Contributor(login, contributions, url, avatar.filter(_.nonEmpty))

}
