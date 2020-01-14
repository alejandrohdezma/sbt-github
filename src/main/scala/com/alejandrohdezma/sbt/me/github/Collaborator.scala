package com.alejandrohdezma.sbt.me.github

import com.alejandrohdezma.sbt.me.json.Decoder
import com.alejandrohdezma.sbt.me.syntax.json._

/** Represents a repository collaborator */
final case class Collaborator private[me] (
    login: String,
    url: String,
    userUrl: String,
    name: Option[String],
    email: Option[String],
    avatar: Option[String]
)

object Collaborator {

  /**
   * Creates a new collaborator
   *
   * @param login the Github login ID for the collaborator
   * @param name the collaborator's full name
   * @param url the collaborator's URL. It may link to its Github profile or personal webpage.
   * @param email the collaborator's email, optional
   * @param avatar the collaborator's avatar URL, optional
   * @return a new collaborator
   */
  @SuppressWarnings(Array("scalafix:DisableSyntax.defaultArgs"))
  def apply(
      login: String,
      name: String,
      url: String,
      email: Option[String] = None,
      avatar: Option[String] = None
  ): Collaborator =
    new Collaborator(login, url, "", Some(name), email, avatar)

  implicit val CollaboratorDecoder: Decoder[Collaborator] = json =>
    for {
      login   <- json.get[String]("login")
      url     <- json.get[String]("html_url")
      userUrl <- json.get[String]("url")
      avatar  <- json.get[Option[String]]("avatar_url")
    } yield Collaborator(login, url, userUrl, None, None, avatar.filter(_.nonEmpty))

}
