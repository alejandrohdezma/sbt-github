package com.alejandrohdezma.sbt.me.github

import com.alejandrohdezma.sbt.me.json.Decoder
import com.alejandrohdezma.sbt.me.syntax.json._

/** Represents a repository's license */
final case class License(id: String, url: String)

object License {

  implicit val LicenseDecoder: Decoder[License] = json =>
    for {
      spdxId <- json.get[String]("spdx_id")
      url    <- json.get[String]("url")
    } yield License(spdxId, url)

}
