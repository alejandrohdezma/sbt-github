package com.alejandrohdezma.sbt.me.github

import sbt.URL

import com.alejandrohdezma.sbt.me.github.Repository.License

/** Represents a repository in Github */
final case class Repository(description: String, license: License, html_url: String) {

  /** Returns the license extracted from github in the format that SBT is expecting */
  def licenses: List[(String, URL)] = List(license.spdx_id -> sbt.url(license.url))

}

object Repository {

  /** Represents a repository's license */
  final case class License(spdx_id: String, url: String)

}
