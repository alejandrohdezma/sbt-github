package com.alejandrohdezma.sbt.me.github

import com.alejandrohdezma.sbt.me.json.Decoder
import com.alejandrohdezma.sbt.me.syntax.json._

final case class User(name: Option[String], email: Option[String])

object User {

  implicit val decoder: Decoder[User] = json =>
    for {
      name  <- json.get[Option[String]]("name")
      email <- json.get[Option[String]]("email")
    } yield User(name.filter(_.nonEmpty), email.filter(_.nonEmpty))

}
