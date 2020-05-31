/*
 * Copyright 2019-2020 Alejandro Hernández <https://github.com/alejandrohdezma>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alejandrohdezma.sbt.github.github

import sbt.librarymanagement.Developer

/** Represents a repository's list of collaborators */
final case class Collaborators(list: List[Collaborator]) {

  /** Includes the provided list of collaborators to the current list, removing duplicates */
  def include(collaborators: List[Collaborator]): Collaborators =
    Collaborators {
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

    Developer(login, name.getOrElse(login), email.getOrElse(""), url)
  }

  /** Returns this list of collaborators in markdown format */
  lazy val markdown: String =
    list.map { collaborator =>
      import collaborator._

      val image = avatar.map(avatarUrl => s"![$login]($avatarUrl&s=20) ").getOrElse("")

      val definitiveName = name
        .filter(_.nonEmpty)
        .filter(!_.contentEquals(login))
        .map(_ + s" ($login)")
        .getOrElse(login)

      s"""- [$image**$definitiveName**]($url)"""
    }.mkString("\n")

}

object Collaborators {

  def apply(collaborators: Collaborator*): Collaborators = new Collaborators(collaborators.toList)

}
