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

    "return list of collaborators including ones" >> {
      val collaborators = Collaborators(
        List(
          Collaborator("me", sbt.url("http://example.com/me"), None, Some("Me"), None, None),
          Collaborator("you", sbt.url("http://example.com/you"), None, Some("You"), None, None)
        )
      )

      val extra =
        List(Collaborator("him", sbt.url("http://example.com/him"), None, Some("Him"), None, None))

      val expected = Collaborators(
        List(
          Collaborator("him", sbt.url("http://example.com/him"), None, Some("Him"), None, None),
          Collaborator("me", sbt.url("http://example.com/me"), None, Some("Me"), None, None),
          Collaborator("you", sbt.url("http://example.com/you"), None, Some("You"), None, None)
        )
      )

      collaborators.include(extra) must be equalTo expected
    }

    "remove duplicates" >> {
      val collaborators = Collaborators(
        List(
          Collaborator("me", sbt.url("http://example.com/me"), None, Some("Me"), None, None),
          Collaborator("you", sbt.url("http://example.com/you"), None, Some("You"), None, None)
        )
      )

      val extra =
        List(Collaborator("me", sbt.url("http://example.com/meme"), None, Some("MeMe"), None, None))

      val expected = Collaborators(
        List(
          Collaborator("me", sbt.url("http://example.com/me"), None, Some("Me"), None, None),
          Collaborator("you", sbt.url("http://example.com/you"), None, Some("You"), None, None)
        )
      )

      collaborators.include(extra) must be equalTo expected
    }

  }

  "Collaborators.markdown" should {

    "return collaborator list as markdown" >> {
      val collaborators = Collaborators(
        List(
          Collaborator(
            "her",
            sbt.url("http://example.com/her"),
            None,
            Some("Her"),
            None,
            Some(sbt.url("http://example.com/her.png"))
          ),
          Collaborator("him", sbt.url("http://example.com/him"), None, Some("Him"), None, None),
          Collaborator(
            "it",
            sbt.url("http://example.com/it"),
            None,
            Some("It"),
            Some("it@example.com"),
            Some(sbt.url("http://example.com/it.png"))
          ),
          Collaborator(
            "me",
            sbt.url("http://example.com/me"),
            None,
            Some("Me"),
            Some("me@example.com"),
            None
          ),
          Collaborator("you", sbt.url("http://example.com/you"), None, Some(""), None, None)
        )
      )

      val markdown = collaborators.markdown

      val expected =
        """- [![her](http://example.com/her.png&s=20) **Her (her)**](http://example.com/her)
          |- [**Him (him)**](http://example.com/him)
          |- [![it](http://example.com/it.png&s=20) **It (it)**](http://example.com/it)
          |- [**Me (me)**](http://example.com/me)
          |- [**you**](http://example.com/you)""".stripMargin

      markdown must be equalTo expected
    }

  }

  "Collaborators.developers" should {

    "return collaborators as list of developers" >> {
      val collaborators = Collaborators(
        List(
          Collaborator("her", sbt.url("http://example.com/her"), None, Some("Her"), None, None),
          Collaborator("him", sbt.url("http://example.com/him"), None, Some("Him"), None, None),
          Collaborator(
            "it",
            sbt.url("http://example.com/it"),
            None,
            None,
            Some("it@example.com"),
            None
          ),
          Collaborator("me", sbt.url("http://example.com/me"), None, Some("Me"), None, None),
          Collaborator("you", sbt.url("http://example.com/you"), None, Some(""), None, None)
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
