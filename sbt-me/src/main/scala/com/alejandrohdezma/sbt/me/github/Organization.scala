package com.alejandrohdezma.sbt.me.github

import com.alejandrohdezma.sbt.me.json.Decoder
import com.alejandrohdezma.sbt.me.syntax.json._

/** Represents a repository's organization */
final case class Organization(name: Option[String], url: Option[String])

object Organization {

  implicit val OrganizationDecoder: Decoder[Organization] = json =>
    for {
      name <- json.get[Option[String]]("name")
      url  <- json.get[Option[String]]("blog")
    } yield Organization(name, url)

}
