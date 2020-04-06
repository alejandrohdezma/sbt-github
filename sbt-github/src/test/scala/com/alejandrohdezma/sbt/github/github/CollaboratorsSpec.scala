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

import sbt.Developer

import com.alejandrohdezma.sbt.github._
import org.specs2.mutable.Specification

class CollaboratorsSpec extends Specification {

  "Collaborators.include" should {

    "return list of collaborators including ones" >> {
      val collaborators = Collaborators(
        Collaborator("me", "Me", sbt.url("http://example.com/me")),
        Collaborator("you", "You", sbt.url("http://example.com/you"))
      )

      val extra: List[Collaborator] =
        List(Collaborator("him", "Him", sbt.url("http://example.com/him")))

      val expected = Collaborators(
        Collaborator("him", "Him", sbt.url("http://example.com/him")),
        Collaborator("me", "Me", sbt.url("http://example.com/me")),
        Collaborator("you", "You", sbt.url("http://example.com/you"))
      )

      collaborators.include(extra) must be equalTo expected
    }

    "remove duplicates" >> {
      val collaborators = Collaborators(
        Collaborator("me", "Me", sbt.url("http://example.com/me")),
        Collaborator("you", "You", sbt.url("http://example.com/you"))
      )

      val extra: List[Collaborator] =
        List(Collaborator("me", "MeMe", sbt.url("http://example.com/meme")))

      val expected = Collaborators(
        Collaborator("me", "Me", sbt.url("http://example.com/me")),
        Collaborator("you", "You", sbt.url("http://example.com/you"))
      )

      collaborators.include(extra) must be equalTo expected
    }

  }

  "Collaborators.markdown" should {

    "return collaborator list as markdown" >> {
      val collaborators = Collaborators(
        Collaborator(
          "her",
          "Her",
          sbt.url("http://example.com/her"),
          sbt.url("http://example.com/her.png")
        ),
        Collaborator("him", "Him", sbt.url("http://example.com/him")),
        Collaborator(
          "it",
          "It",
          sbt.url("http://example.com/it"),
          "it@example.com",
          sbt.url("http://example.com/it.png")
        ),
        Collaborator("me", "Me", sbt.url("http://example.com/me"), "me@example.com"),
        Collaborator("you", "you", sbt.url("http://example.com/you"))
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
        Collaborator("her", "Her", sbt.url("http://example.com/her")),
        Collaborator("him", "Him", sbt.url("http://example.com/him")),
        Collaborator("it", "it", sbt.url("http://example.com/it"), "it@example.com"),
        Collaborator("me", "Me", sbt.url("http://example.com/me")),
        Collaborator("you", "", sbt.url("http://example.com/you"))
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
