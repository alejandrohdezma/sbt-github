package com.alejandrohdezma.sbt.me.github.urls

import com.alejandrohdezma.sbt.me.http._
import com.alejandrohdezma.sbt.me.json.Decoder
import com.alejandrohdezma.sbt.me.syntax.json._

final case class Repository(base: String) {

  def get(owner: String, repo: String): String =
    base.replace("{owner}", owner).replace("{repo}", repo)

}

object Repository {

  implicit def repository(implicit auth: Authentication): Repository =
    client
      .get[Repository]("https://api.github.com")
      .getOrElse(sys.error("Unable to connect to Github"))

  implicit val RepositoryUrlDecoder: Decoder[Repository] = json =>
    json.get[String]("repository_url").map(Repository(_))

}
