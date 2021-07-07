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

package com.alejandrohdezma.sbt.github.syntax

import scala.util.Failure
import scala.util.Success

import com.alejandrohdezma.sbt.github.error._
import com.alejandrohdezma.sbt.github.json.Json
import com.alejandrohdezma.sbt.github.json.error._
import com.alejandrohdezma.sbt.github.syntax.json._

class JsonSyntaxSuite extends munit.FunSuite {

  test("Json.Object.get should return value at path if present") {
    val json = Json.Object(Map("miau" -> Json.Number(42d)))

    assertEquals(json.get("miau"), Json.Number(42d))
  }

  test("Json.Object.get should return Json.Null if field not present") {
    val json = Json.Object(Map("miau" -> Json.Number(42d)))

    assertEquals(json.get("cat"), Json.Null)
  }

  test("Json.value.get should return NotAJsonObject if json is not a JSON object") {
    val json: Json.Value = Json.True

    assertEquals(json.get[Boolean]("miau"), Failure(NotAJSONObject(json)))
  }

  test("Json.value.get should return NotFound with path if not present") {
    val json: Json.Value = Json.Object(Map("miau" -> Json.Number(42d)))

    assertEquals(json.get[Int]("cat"), Failure(InvalidPath("cat", NotFound)))
  }

  test("Json.value.get should return requested type if present and decoding succeeds") {
    val json: Json.Value = Json.Object(Map("miau" -> Json.Number(42d)))

    assertEquals(json.get[Int]("miau"), Success(42))
  }

  test("Json.value.get should return requested type on a path chain") {
    val json: Json.Value = Json.Object(
      Map("m1" -> Json.Object(Map("m2" -> Json.Object(Map("m3" -> Json.Number(42d))))))
    )

    assertEquals(json.get[Int]("m1", "m2", "m3"), Success(42))
  }

  test("Json.value.get should keep failed path on decoding failure") {
    val json: Json.Value = Json.Object(
      Map("m1" -> Json.Object(Map("m2" -> Json.Object(Map("m3" -> Json.Text("miau"))))))
    )

    val expected =
      InvalidPath("m1", InvalidPath("m2", InvalidPath("m3", NotANumber(Json.Text("miau")))))

    assertEquals(json.get[Int]("m1", "m2", "m3"), Failure(expected))
  }

  test("Json.value.get should keep failed path on not a json object") {
    val json: Json.Value = Json.Object(Map("m1" -> Json.Object(Map("m2" -> Json.Text("miau")))))

    val expected = InvalidPath("m1", InvalidPath("m2", NotAJSONObject(Json.Text("miau"))))

    assertEquals(json.get[Int]("m1", "m2", "m3"), Failure(expected))
  }

  test("Json.value.get should keep failed path on not found") {
    val json: Json.Value = Json.Object(Map("m1" -> Json.Object(Map())))

    val expected = InvalidPath("m1", InvalidPath("m2", NotFound))

    assertEquals(json.get[Int]("m1", "m2", "m3"), Failure(expected))
  }

  test("Json.value.get should propagate Decoder[A] failure") {
    val json: Json.Value = Json.Object(Map("miau" -> Json.Number(42d)))

    val expected = InvalidPath("miau", NotABoolean(Json.Number(42d)))

    assertEquals(json.get[Boolean]("miau"), Failure(expected))
  }

  test("`/` extractor should allow matching path failures") {
    InvalidPath("miau", NotFound) match {
      case "miau" / NotFound => ()
    }
  }

  test("`/` extractor should allow matching nested Path failures") {
    InvalidPath("miau", InvalidPath("cat", NotFound)) match {
      case "miau" / ("cat" / NotFound) => ()
    }
  }

  test("`/` extractor should not match other failures") {
    val fail = NotFound

    assertEquals(/.unapply(fail), None)
  }

}
