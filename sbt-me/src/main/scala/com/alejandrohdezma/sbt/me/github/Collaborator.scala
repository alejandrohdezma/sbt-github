package com.alejandrohdezma.sbt.me.github

import sbt.util.Logger

import com.alejandrohdezma.sbt.me.http.{client, Authentication}
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

  /** Obtains a collaborator information from its Github login ID */
  def github(id: String)(implicit auth: Authentication): Logger => Collaborator = { implicit log =>
    val userUrl = implicitly[urls.User].get(id)

    client.get[User](userUrl).map { user =>
      new Collaborator(user.login, user.url, userUrl, user.name, user.email, user.avatar)
    } fold (_ => sys.error(s"Unable to get info for user $id"), identity)
  }

  /**
   * Creates a new collaborator
   *
   * @param login the Github login ID for the collaborator
   * @param name the collaborator's full name
   * @param url the collaborator's URL. It may link to its Github profile or personal webpage.
   * @return a new collaborator
   */
  def apply(login: String, name: String, url: String): Logger => Collaborator = { _ =>
    new Collaborator(login, url, "", Some(name), None, None)
  }

  /**
   * Creates a new collaborator
   *
   * @param login the Github login ID for the collaborator
   * @param name the collaborator's full name
   * @param url the collaborator's URL. It may link to its Github profile or personal webpage.
   * @param email the collaborator's email
   * @return a new collaborator
   */
  def apply(login: String, name: String, url: String, email: String): Logger => Collaborator = {
    _ =>
      new Collaborator(login, url, "", Some(name), Some(email), None)
  }

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
  def apply(
      login: String,
      name: String,
      url: String,
      email: Option[String],
      avatar: Option[String]
  ): Logger => Collaborator = { _ =>
    new Collaborator(login, url, "", Some(name), email, avatar)
  }

  implicit val CollaboratorDecoder: Decoder[Collaborator] = json =>
    for {
      login   <- json.get[String]("login")
      url     <- json.get[String]("html_url")
      userUrl <- json.get[String]("url")
      avatar  <- json.get[Option[String]]("avatar_url")
    } yield Collaborator(login, url, userUrl, None, None, avatar.filter(_.nonEmpty))

}
