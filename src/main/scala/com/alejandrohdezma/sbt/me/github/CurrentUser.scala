package com.alejandrohdezma.sbt.me.github

/** Represents the current authenticated user from Github */
final case class CurrentUser(login: String, name: String, email: String, html_url: String)
