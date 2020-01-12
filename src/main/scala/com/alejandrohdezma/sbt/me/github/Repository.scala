package com.alejandrohdezma.sbt.me.github

import sbt.URL

import com.alejandrohdezma.sbt.me.json.Decoder
import com.alejandrohdezma.sbt.me.syntax.json._

/** Represents a repository in Github */
final case class Repository(description: String, license: License, url: String) {

  /** Returns the license extracted from github in the format that SBT is expecting */
  def licenses: List[(String, URL)] = List(license.id -> sbt.url(license.url))

}

object Repository {

  implicit val RepositoryDecoder: Decoder[Repository] = json =>
    for {
      description <- json.get[String]("description")
      license     <- json.get[License]("license")
      url         <- json.get[String]("html_url")
    } yield Repository(description, license, url)

}
