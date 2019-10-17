package com.alejandrohdezma.sbt.me.github

import com.alejandrohdezma.sbt.me.github.Repository.License
import sbt.URL

/** Relevant information about a repository for a POM file */
final case class Repository(description: String, license: License) {

  /** Returns the license extracted from github in the format that SBT is expecting */
  def licenses: List[(String, URL)] = List(license.spdx_id -> sbt.url(license.url))

}

object Repository {

  final case class License(spdx_id: String, url: String)

}
