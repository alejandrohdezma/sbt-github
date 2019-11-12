package com.alejandrohdezma.sbt.me.github

import sbt.{url, Developer}

/** Represents the current authenticated user from Github */
final case class CurrentUser(login: String, name: String, email: String, html_url: String) {

  /** The current user as an sbt `Developer` */
  def developer: Developer = Developer(login, name, email, url(html_url))

  /** The user's organization extracted from its email */
  def organization: String =
    email.substring(email.indexOf("@") + 1).split('.').reverse.mkString(".")

}
