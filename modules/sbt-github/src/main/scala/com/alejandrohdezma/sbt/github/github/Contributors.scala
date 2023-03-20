/*
 * Copyright 2019-2023 Alejandro Hernández <https://github.com/alejandrohdezma>
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

/** Represents a repository's list of contributors */
final case class Contributors(list: List[Contributor]) {

  /** Returns this list of contributors in markdown format */
  @deprecated("Use markdownList instead", "0.11.0")
  lazy val markdown: String = markdownList

  /** Returns this list of contributors in markdown format */
  lazy val markdownList: String =
    list.map { contributor =>
      val image =
        contributor.avatar.map(url => s"![${contributor.login}]($url&s=20) ").getOrElse("")

      s"""- [$image**${contributor.login}**](${contributor.url})"""
    }.mkString("\n")

  /** Returns this list of contributors as a markdown table */
  lazy val markdownTable: String =
    if (list.isEmpty) "No contributors found"
    else
      list
        .grouped(7)
        .toList
        .map { contributors =>
          val images = contributors.map { contributor =>
            val url =
              contributor.avatar.getOrElse(s"https://www.gravatar.com/avatar/${contributor.login}?d=identicon")

            s"""<a href="${contributor.url}"><img alt="${contributor.login}" src="$url&s=120" width="120px" /></a>"""
          }.mkString("| ", " | ", " |")

          val separators = List.fill(contributors.size)(":--:").mkString("| ", " | ", " |")

          val logins = contributors.map { contributor =>
            s"""<a href="${contributor.url}"><sub><b>${contributor.login}</b></sub></a>"""
          }.mkString("| ", " | ", " |")

          s"$images\n$separators\n$logins"
        }
        .mkString("\n\n")

}
