/*
 * Copyright 2019-2024 Alejandro Hernández <https://github.com/alejandrohdezma>
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

package com.alejandrohdezma.sbt.github.json

import scala.util.Success

class JsonSuite extends munit.FunSuite {

  test("Json.parse should decode Json.Text") {
    val json = """"a String""""

    val expected: Json.Value = Json.Text("a String")

    assertEquals(Json.parse(json), Success(expected))
  }

  test("Json.parse should decode Json.Number") {
    val json = "42"

    val expected: Json.Value = Json.Number(42)

    assertEquals(Json.parse(json), Success(expected))
  }

  test("Json.parse should decode Json.True") {
    val json = "true"

    val expected: Json.Value = Json.True

    assertEquals(Json.parse(json), Success(expected))
  }

  test("Json.parse should decode Json.False") {
    val json = "false"

    val expected: Json.Value = Json.False

    assertEquals(Json.parse(json), Success(expected))
  }

  test("Json.parse should decode Json.Null") {
    val json = "null"

    val expected: Json.Value = Json.Null

    assertEquals(Json.parse(json), Success(expected))
  }

  test("Json.parse should decode Json.Collection") {
    val json = "[1, 2, 3, 4]"

    val expected: Json.Value = Json.Collection(List(1d, 2d, 3d, 4d).map(Json.Number))

    assertEquals(Json.parse(json), Success(expected))
  }

  test("Json.parse should decode Json.Object") {
    val json = """{
        "string": "a String",
        "number": 42,
        "true": true,
        "false": false,
        "null": null,
        "array": [1, 2, 3, 4]
      }"""

    val expected: Json.Value = Json.Object(
      Map(
        "string" -> Json.Text("a String"),
        "number" -> Json.Number(42),
        "true"   -> Json.True,
        "false"  -> Json.False,
        "null"   -> Json.Null,
        "array"  -> Json.Collection(List(1d, 2d, 3d, 4d).map(Json.Number))
      )
    )

    assertEquals(Json.parse(json), Success(expected))

  }

  test("Json.parse should decode long strings") {
    val json = List.fill(3041)("x").mkString("\"", "", "\"")
    assert(Json.parse(json).isSuccess)
  }

}
