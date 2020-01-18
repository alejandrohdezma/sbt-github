package com.alejandrohdezma.sbt.me.http

trait Authentication {

  def header: String

}

object Authentication {

  implicit def apply: Authentication = {
    val key = "GITHUB_TOKEN"

    sys.env
      .get(key)
      .map(Token)
      .getOrElse(
        sys.error {
          s"You need to add an environment variable named $key with a Github personal access token."
        }
      )
  }

  final case class Token(value: String) extends Authentication {

    override def header: String = s"token $value"

  }

}
