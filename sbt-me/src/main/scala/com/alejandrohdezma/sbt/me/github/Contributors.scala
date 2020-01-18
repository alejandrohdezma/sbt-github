package com.alejandrohdezma.sbt.me.github

/** Represents a repository's list of contributors */
final case class Contributors(list: List[Contributor]) {

  /** Returns this list of contributors in markdown format */
  lazy val markdown: String =
    list.map { contributor =>
      val image =
        contributor.avatar.map(url => s"![${contributor.login}]($url&s=20) ").getOrElse("")

      s"""- [$image**${contributor.login}**](${contributor.url})"""
    }.mkString("\n")

}
