package com.alejandrohdezma.sbt.github.github

import com.alejandrohdezma.sbt.github.json.Decoder
import com.alejandrohdezma.sbt.github.syntax.json._

/** Represents a repository's license */
final case class License(id: String, url: String)

object License {

  implicit val LicenseDecoder: Decoder[License] = json =>
    for {
      spdxId <- json.get[String]("spdx_id")
      url    <- json.get[String]("url")
    } yield License(spdxId, url)

}
