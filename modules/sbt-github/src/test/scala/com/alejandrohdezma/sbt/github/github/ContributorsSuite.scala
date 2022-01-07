/*
 * Copyright 2019-2022 Alejandro Hernández <https://github.com/alejandrohdezma>
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

class ContributorsSuite extends munit.FunSuite {

  test("Contributors.markdown should return contributor list as markdown") {
    val contributors = Contributors(
      List(
        Contributor("me", 42, "http://example.com/me", Some("http://example.com/me.png")),
        Contributor("him", 6, "http://example.com/him", None),
        Contributor("you", 2, "http://example.com/you", None)
      )
    )

    @nowarn
    val markdown = contributors.markdown

    val expected =
      """- [![me](http://example.com/me.png&s=20) **me**](http://example.com/me)
        |- [**him**](http://example.com/him)
        |- [**you**](http://example.com/you)""".stripMargin

    assertNoDiff(markdown, expected)
  }

  test("Contributors.markdownTable should return contributor list as a markdown table") {
    val contributors = Contributors(
      List(
        Contributor("me", 1, "http://example.com/me", Some("http://example.com/me.png")),
        Contributor("you", 1, "http://example.com/you", None),
        Contributor("him", 1, "http://example.com/him", None),
        Contributor("her", 1, "http://example.com/her", Some("http://example.com/me.png")),
        Contributor("it", 1, "http://example.com/it", Some("http://example.com/it.png")),
        Contributor("we", 1, "http://example.com/we", None),
        Contributor("they", 1, "http://example.com/they", None)
      )
    )

    val markdown = contributors.markdownTable

    val expected =
      """|| <a href="http://example.com/me"><img alt="me" src="http://example.com/me.png&s=120" width="120px" /></a> | <a href="http://example.com/you"><img alt="you" src="https://www.gravatar.com/avatar/you?d=identicon&s=120" width="120px" /></a> | <a href="http://example.com/him"><img alt="him" src="https://www.gravatar.com/avatar/him?d=identicon&s=120" width="120px" /></a> | <a href="http://example.com/her"><img alt="her" src="http://example.com/me.png&s=120" width="120px" /></a> | <a href="http://example.com/it"><img alt="it" src="http://example.com/it.png&s=120" width="120px" /></a> | <a href="http://example.com/we"><img alt="we" src="https://www.gravatar.com/avatar/we?d=identicon&s=120" width="120px" /></a> | <a href="http://example.com/they"><img alt="they" src="https://www.gravatar.com/avatar/they?d=identicon&s=120" width="120px" /></a> |
         || :--: | :--: | :--: | :--: | :--: | :--: | :--: |
         || <a href="http://example.com/me"><sub><b>me</b></sub></a> | <a href="http://example.com/you"><sub><b>you</b></sub></a> | <a href="http://example.com/him"><sub><b>him</b></sub></a> | <a href="http://example.com/her"><sub><b>her</b></sub></a> | <a href="http://example.com/it"><sub><b>it</b></sub></a> | <a href="http://example.com/we"><sub><b>we</b></sub></a> | <a href="http://example.com/they"><sub><b>they</b></sub></a> |""".stripMargin

    assertNoDiff(markdown, expected)
  }

}
