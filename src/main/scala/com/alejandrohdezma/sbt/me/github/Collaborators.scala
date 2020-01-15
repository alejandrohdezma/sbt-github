package com.alejandrohdezma.sbt.me.github

import sbt.librarymanagement.Developer

/** Represents a repository's list of collaborators */
final case class Collaborators(list: List[Collaborator]) {

  /** Includes the provided list of collaborators to the current list, removing duplicates */
  private[me] def include(collaborators: List[Collaborator]): Collaborators = Collaborators {
    (list ++ collaborators)
      .groupBy(_.login)
      .values
      .toList
      .map(_.head) /* scalafix:ok */
      .sortBy(collaborator => collaborator.name -> collaborator.login)
  }

  /** Returns this list of collaborators as SBT developers */
  lazy val developers: List[Developer] = list.map { collaborator =>
    import collaborator._

    Developer(login, name.getOrElse(login), email.getOrElse(""), sbt.url(url))
  }

  /** Returns this list of collaborators in markdown format */
  lazy val markdown: String =
    list.map { collaborator =>
      import collaborator._

      val image = avatar.map(avatarUrl => s"![$login]($avatarUrl&s=20) ").getOrElse("")

      val definitiveName = name.filter(_.nonEmpty).getOrElse(login)

      val prettyName =
        if (definitiveName.contentEquals(login)) login else s"$definitiveName ($login)"

      Option(url)
        .filter(_.nonEmpty)
        .fold(s"""- $image**$prettyName**""")(u => s"""- [$image**$prettyName**]($u)""")
    }.mkString("\n")

}
