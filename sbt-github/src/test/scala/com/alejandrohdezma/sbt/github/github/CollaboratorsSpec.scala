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

import org.specs2.mutable.Specification

class CollaboratorsSpec extends Specification {

  "Collaborators.include" should {

    "return list of collaborators including new ones" >> {
      val collaborators = Collaborators(
        List(
          new Collaborator("me", "example.com/me", "", Some("Me"), None, None),
          new Collaborator("you", "example.com/you", "", Some("You"), None, None)
        )
      )

      val extra = List(new Collaborator("him", "example.com/him", "", Some("Him"), None, None))

      val expected = Collaborators(
        List(
          new Collaborator("him", "example.com/him", "", Some("Him"), None, None),
          new Collaborator("me", "example.com/me", "", Some("Me"), None, None),
          new Collaborator("you", "example.com/you", "", Some("You"), None, None)
        )
      )

      collaborators.include(extra) must be equalTo expected
    }

    "remove duplicates" >> {
      val collaborators = Collaborators(
        List(
          new Collaborator("me", "example.com/me", "", Some("Me"), None, None),
          new Collaborator("you", "example.com/you", "", Some("You"), None, None)
        )
      )

      val extra = List(new Collaborator("me", "example.com/meme", "", Some("MeMe"), None, None))

      val expected = Collaborators(
        List(
          new Collaborator("me", "example.com/me", "", Some("Me"), None, None),
          new Collaborator("you", "example.com/you", "", Some("You"), None, None)
        )
      )

      collaborators.include(extra) must be equalTo expected
    }

  }

  "Collaborators.markdown" should {

    "return collaborator list as markdown" >> {
      val collaborators = Collaborators(
        List(
          new Collaborator(
            "her",
            "example.com/her",
            "",
            Some("Her"),
            None,
            Some("example.com/her.png")
          ),
          new Collaborator("him", "example.com/him", "", Some("Him"), None, None),
          new Collaborator(
            "it",
            "example.com/it",
            "",
            Some("It"),
            Some("it@example.com"),
            Some("example.com/it.png")
          ),
          new Collaborator("me", "example.com/me", "", Some("Me"), Some("me@example.com"), None),
          new Collaborator("you", "example.com/you", "", Some(""), None, None)
        )
      )

      val markdown = collaborators.markdown

      val expected =
        """- [![her](example.com/her.png&s=20) **Her (her)**](example.com/her)
          |- [**Him (him)**](example.com/him)
          |- [![it](example.com/it.png&s=20) **It (it)**](example.com/it)
          |- [**Me (me)**](example.com/me)
          |- [**you**](example.com/you)""".stripMargin

      markdown must be equalTo expected
    }

  }

  "Collaborators.developers" should {

    "return collaborators as list of developers" >> {
      val collaborators = Collaborators(
        List(
          new Collaborator("her", "http://example.com/her", "", Some("Her"), None, None),
          new Collaborator("him", "http://example.com/him", "", Some("Him"), None, None),
          new Collaborator("it", "http://example.com/it", "", None, Some("it@example.com"), None),
          new Collaborator("me", "http://example.com/me", "", Some("Me"), None, None),
          new Collaborator("you", "http://example.com/you", "", Some(""), None, None)
        )
      )

      val expected = List(
        Developer("her", "Her", "", sbt.url("http://example.com/her")),
        Developer("him", "Him", "", sbt.url("http://example.com/him")),
        Developer("it", "it", "it@example.com", sbt.url("http://example.com/it")),
        Developer("me", "Me", "", sbt.url("http://example.com/me")),
        Developer("you", "", "", sbt.url("http://example.com/you"))
      )

      collaborators.developers must be equalTo expected
    }

  }

}
