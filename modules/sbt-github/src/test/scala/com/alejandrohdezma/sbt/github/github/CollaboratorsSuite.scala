/*
 * Copyright 2019-2021 Alejandro Hernández <https://github.com/alejandrohdezma>
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

import scala.annotation.nowarn

import sbt.Developer

import com.alejandrohdezma.sbt.github._

class CollaboratorsSuite extends munit.FunSuite {

  test("Collaborators.include should return list of collaborators including ones") {
    val collaborators = Collaborators(
      Collaborator("me", "Me", url"http://example.com/me"),
      Collaborator("you", "You", url"http://example.com/you")
    )

    val extra: List[Collaborator] =
      List(Collaborator("him", "Him", url"http://example.com/him"))

    val expected = Collaborators(
      Collaborator("him", "Him", url"http://example.com/him"),
      Collaborator("me", "Me", url"http://example.com/me"),
      Collaborator("you", "You", url"http://example.com/you")
    )

    assertEquals(collaborators.include(extra), expected)
  }

  test("Collaborators.include should remove duplicates") {
    val collaborators = Collaborators(
      Collaborator("me", "Me", url"http://example.com/me"),
      Collaborator("you", "You", url"http://example.com/you")
    )

    val extra: List[Collaborator] =
      List(Collaborator("me", "MeMe", url"http://example.com/meme"))

    val expected = Collaborators(
      Collaborator("me", "Me", url"http://example.com/me"),
      Collaborator("you", "You", url"http://example.com/you")
    )

    assertEquals(collaborators.include(extra), expected)
  }

  test("Collaborators.markdown should return collaborator list as markdown") {
    val collaborators = Collaborators(
      Collaborator(
        "her",
        "Her",
        url"http://example.com/her",
        url"http://example.com/her.png"
      ),
      Collaborator("him", "Him", url"http://example.com/him"),
      Collaborator(
        "it",
        "It",
        url"http://example.com/it",
        "it@example.com",
        url"http://example.com/it.png"
      ),
      Collaborator("me", "Me", url"http://example.com/me", "me@example.com"),
      Collaborator("you", "you", url"http://example.com/you")
    )

    @nowarn
    val markdown = collaborators.markdown

    val expected =
      """- [![her](http://example.com/her.png&s=20) **Her (her)**](http://example.com/her)
        |- [**Him (him)**](http://example.com/him)
        |- [![it](http://example.com/it.png&s=20) **It (it)**](http://example.com/it)
        |- [**Me (me)**](http://example.com/me)
        |- [**you**](http://example.com/you)""".stripMargin

    assertNoDiff(markdown, expected)
  }

  test("Collaborators.markdownTable should return collaborator list as a markdown table") {
    val collaborators = Collaborators(
      Collaborator(
        "her",
        "Her",
        url"http://example.com/her",
        url"http://example.com/her.png"
      ),
      Collaborator("him", "Him", url"http://example.com/him"),
      Collaborator(
        "it",
        "It",
        url"http://example.com/it",
        "it@example.com",
        url"http://example.com/it.png"
      ),
      Collaborator("me", "Me", url"http://example.com/me", "me@example.com"),
      Collaborator("you", "you", url"http://example.com/you")
    )

    val markdown = collaborators.markdownTable

    val expected =
      """|| <a href="http://example.com/her"><img alt="her" src="http://example.com/her.png&s=120" width="120px" /></a> | <a href="http://example.com/him"><img alt="him" src="https://www.gravatar.com/avatar/him?d=identicon&s=120" width="120px" /></a> | <a href="http://example.com/it"><img alt="it" src="http://example.com/it.png&s=120" width="120px" /></a> | <a href="http://example.com/me"><img alt="me" src="https://www.gravatar.com/avatar/me?d=identicon&s=120" width="120px" /></a> | <a href="http://example.com/you"><img alt="you" src="https://www.gravatar.com/avatar/you?d=identicon&s=120" width="120px" /></a> |
         || :--: | :--: | :--: | :--: | :--: |
         || <a href="http://example.com/her"><sub><b>her</b></sub></a> | <a href="http://example.com/him"><sub><b>him</b></sub></a> | <a href="http://example.com/it"><sub><b>it</b></sub></a> | <a href="http://example.com/me"><sub><b>me</b></sub></a> | <a href="http://example.com/you"><sub><b>you</b></sub></a> |""".stripMargin

    assertNoDiff(markdown, expected)
  }

  test("Collaborators.developers should return collaborators as list of developers") {
    val collaborators = Collaborators(
      Collaborator("her", "Her", url"http://example.com/her"),
      Collaborator("him", "Him", url"http://example.com/him"),
      Collaborator("it", "it", url"http://example.com/it", "it@example.com"),
      Collaborator("me", "Me", url"http://example.com/me"),
      Collaborator("you", "", url"http://example.com/you")
    )

    val expected = List(
      Developer("her", "Her", "", url"http://example.com/her"),
      Developer("him", "Him", "", url"http://example.com/him"),
      Developer("it", "it", "it@example.com", url"http://example.com/it"),
      Developer("me", "Me", "", url"http://example.com/me"),
      Developer("you", "", "", url"http://example.com/you")
    )

    assertEquals(collaborators.developers, expected)
  }

}
