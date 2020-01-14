package com.alejandrohdezma.sbt.me.github

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

}
