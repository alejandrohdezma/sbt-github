package com.alejandrohdezma.sbt.github.github

import java.time.ZonedDateTime

import com.alejandrohdezma.sbt.github.json.Decoder
import com.alejandrohdezma.sbt.github.syntax.json._

/** Represents a repository release */
final case class Release(name: String, tag: String, publishDate: Option[ZonedDateTime]) {

  lazy val isPublished: Boolean = publishDate.nonEmpty

}

object Release {

  implicit val ReleaseDecoder: Decoder[Release] = json =>
    for {
      name        <- json.get[String]("name")
      tag         <- json.get[String]("tag_name")
      publishDate <- json.get[Option[ZonedDateTime]]("published_at")
    } yield Release(name, tag, publishDate)

}
