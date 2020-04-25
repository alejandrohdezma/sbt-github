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

package com.alejandrohdezma.sbt.github.json

import java.time.ZoneOffset.UTC
import java.time.ZonedDateTime

import scala.util.Failure
import scala.util.Success

import sbt.URL

import com.alejandrohdezma.sbt.github.error._
import com.alejandrohdezma.sbt.github.json.error._
import com.alejandrohdezma.sbt.github.syntax.json._

class DecoderSuite extends munit.FunSuite {

  test("Decoder[String] should decode Json.Text") {
    val json = Json.Text("miau")

    val expected = "miau"

    assertEquals(json.as[String], Success(expected))
  }

  test("Decoder[String] should return NotFound on null") {
    val json = Json.Null

    assertEquals(json.as[String], Failure(NotFound))
  }

  test("Decoder[String] should return NotAString for everything else") {
    val json = Json.Number(42)

    assertEquals(json.as[String], Failure(NotAString(json)))
  }

  test("Decoder[Long] should decode Json.Number") {
    val json = Json.Number(42)

    val expected = 42L

    assertEquals(json.as[Long], Success(expected))
  }

  test("Decoder[Long] should return NotFound on null") {
    val json = Json.Null

    assertEquals(json.as[Long], Failure(NotFound))
  }

  test("Decoder[Long] should return NotANumber for everything else") {
    val json = Json.Text("miau")

    assertEquals(json.as[Long], Failure(NotANumber(json)))
  }

  test("Decoder[Int] should decode Json.Number") {
    val json = Json.Number(42)

    val expected = 42

    assertEquals(json.as[Int], Success(expected))
  }

  test("Decoder[Int] should return NotFound on null") {
    val json = Json.Null

    assertEquals(json.as[Int], Failure(NotFound))
  }

  test("Decoder[Int] should return NotANumber for everything else") {
    val json = Json.Text("miau")

    assertEquals(json.as[Int], Failure(NotANumber(json)))
  }

  test("Decoder[Double] should decode Json.Number") {
    val json = Json.Number(42)

    val expected = 42d

    assertEquals(json.as[Double], Success(expected))
  }

  test("Decoder[Double] should return NotFound on null") {
    val json = Json.Null

    assertEquals(json.as[Double], Failure(NotFound))
  }

  test("Decoder[Double] should return NotANumber for everything else") {
    val json = Json.Text("miau")

    assertEquals(json.as[Double], Failure(NotANumber(json)))
  }

  test("Decoder[Boolean] should decode Json.True") {
    val json = Json.True

    assertEquals(json.as[Boolean], Success(true))
  }

  test("Decoder[Boolean] should decode Json.False") {
    val json = Json.False

    assertEquals(json.as[Boolean], Success(false))
  }

  test("Decoder[Boolean] should return NotFound on null") {
    val json = Json.Null

    assertEquals(json.as[Boolean], Failure(NotFound))
  }

  test("Decoder[Boolean] should return NotABoolean for everything else") {
    val json = Json.Text("miau")

    assertEquals(json.as[Boolean], Failure(NotABoolean(json)))
  }

  test("Decoder[Option] should not fail on Json.Null") {
    val json = Json.Null

    assertEquals(json.as[Option[Boolean]], Success(Option.empty[Boolean]))
  }

  test("Decoder[Option] should use A's Decoder in case of non-null") {
    val json = Json.True

    assertEquals(json.as[Option[Boolean]], Success(Some(true)))
  }

  test("Decoder[Option] should propagate Decoder[A] failure") {
    val json = Json.Text("miau")

    assertEquals(json.as[Option[Boolean]], Failure(NotABoolean(json)))
  }

  test("Decoder[List] should decode Json.Collection") {
    val json = Json.Collection(List(1d, 2d, 3d).map(Json.Number))

    assertEquals(json.as[List[Int]], Success(List(1, 2, 3)))
  }

  test("Decoder[List] should return NotFound on null") {
    val json = Json.Null

    assertEquals(json.as[List[Int]], Failure(NotFound))
  }

  test("Decoder[List] should return NotAList for everything else") {
    val json = Json.Text("miau")

    assertEquals(json.as[List[Int]], Failure(NotAList(json)))
  }

  test("Decoder[List] should propagate Decoder[A] failure") {
    val json = Json.Collection(List("miau").map(Json.Text))

    assertEquals(json.as[List[Int]], Failure(NotANumber(Json.Text("miau"))))
  }

  test("Decoder[ZonedDateTime] should decode date time") {
    val json = Json.Text("2011-01-26T19:01:12Z")

    val expected = ZonedDateTime.of(2011, 1, 26, 19, 1, 12, 0, UTC)

    assertEquals(json.as[ZonedDateTime], Success(expected))
  }

  test("Decoder[ZonedDateTime] should return NotFound on null") {
    val json = Json.Null

    assertEquals(json.as[ZonedDateTime], Failure(NotFound))
  }

  test("Decoder[ZonedDateTime] should return NotADateTime for texts not containing date times") {
    val json = Json.Text("miau")

    assertEquals(json.as[ZonedDateTime], Failure(NotADateTime(json)))
  }

  test("Decoder[ZonedDateTime] should return NotADateTime for everything else") {
    val json = Json.True

    assertEquals(json.as[ZonedDateTime], Failure(NotADateTime(json)))
  }

  test("Decoder[URL] should decode uris") {
    val json = Json.Text("https://example.com?page=2")

    val expected = sbt.url("https://example.com?page=2")

    assertEquals(json.as[URL], Success(expected))
  }

  test("Decoder[URL] should return NotFound on null") {
    val json = Json.Null

    assertEquals(json.as[URL], Failure(NotFound))
  }

  test("Decoder[URL] should return NotAUrl for texts not containing uris") {
    val json = Json.Text("miau")

    assertEquals(json.as[URL], Failure(NotAUrl(json)))
  }

  test("Decoder[URL] should return NotAUrl for everything else") {
    val json = Json.True

    assertEquals(json.as[URL], Failure(NotAUrl(json)))
  }

}
