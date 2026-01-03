/*
 * Copyright 2019-2026 Alejandro Hernández <https://github.com/alejandrohdezma>
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

import scala.util.Success

import com.alejandrohdezma.sbt.github._
import com.alejandrohdezma.sbt.github.json.Json
import com.alejandrohdezma.sbt.github.syntax.json._

class UserSuite extends munit.FunSuite {

  test("Decoder[User] should treat empty email/name/avatar as None") {
    val json = Json.parse("""{
        "login": "me",
        "html_url": "http://example.com/me",
        "name": "",
        "email": ""
      }""")

    val expected = User("me", url"http://example.com/me", None, None, None)

    assertEquals(json.as[User], Success(expected))
  }

}
