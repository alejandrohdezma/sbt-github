package com.alejandrohdezma.sbt.github.github.urls

import sbt.util.Logger

import com.alejandrohdezma.sbt.github.http._
import com.alejandrohdezma.sbt.github.json.Decoder
import com.alejandrohdezma.sbt.github.syntax.json._

final case class Repository(base: String) {

  def get(owner: String, repo: String): String =
    base.replace("{owner}", owner).replace("{repo}", repo)

}

object Repository {

  implicit def repository(
      implicit auth: Authentication,
      logger: Logger,
      entryPoint: GithubEntryPoint
  ): Repository =
    client
      .get[Repository](entryPoint.value)
      .getOrElse(sys.error("Unable to connect to Github"))

  implicit val RepositoryUrlDecoder: Decoder[Repository] = json =>
    json.get[String]("repository_url").map(Repository(_))

}
