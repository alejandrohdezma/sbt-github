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

import com.alejandrohdezma.sbt.github.error._
import com.alejandrohdezma.sbt.github.json.error._
import com.alejandrohdezma.sbt.github.syntax.json._
import org.specs2.mutable.Specification

class DecoderSpec extends Specification {

  "Decoder[String]" should {

    "decode Json.Text" >> {
      val json = Json.Text("miau")

      val expected = "miau"

      json.as[String] must beSuccessfulTry(expected)
    }

    "return NotFound on null" >> {
      val json = Json.Null

      json.as[String] must beAFailedTry(equalTo(NotFound))
    }

    "return NotAString for everything else" >> {
      val json = Json.Number(42)

      json.as[String] must beAFailedTry(equalTo(NotAString(json)))
    }

  }

  "Decoder[Long]" should {

    "decode Json.Number" >> {
      val json = Json.Number(42)

      val expected = 42L

      json.as[Long] must beSuccessfulTry(expected)
    }

    "return NotFound on null" >> {
      val json = Json.Null

      json.as[Long] must beAFailedTry(equalTo(NotFound))
    }

    "return NotANumber for everything else" >> {
      val json = Json.Text("miau")

      json.as[Long] must beAFailedTry(equalTo(NotANumber(json)))
    }

  }

  "Decoder[Int]" should {

    "decode Json.Number" >> {
      val json = Json.Number(42)

      val expected = 42

      json.as[Int] must beSuccessfulTry(expected)
    }

    "return NotFound on null" >> {
      val json = Json.Null

      json.as[Int] must beAFailedTry(equalTo(NotFound))
    }

    "return NotANumber for everything else" >> {
      val json = Json.Text("miau")

      json.as[Int] must beAFailedTry(equalTo(NotANumber(json)))
    }

  }

  "Decoder[Double]" should {

    "decode Json.Number" >> {
      val json = Json.Number(42)

      val expected = 42d

      json.as[Double] must beSuccessfulTry(expected)
    }

    "return NotFound on null" >> {
      val json = Json.Null

      json.as[Double] must beAFailedTry(equalTo(NotFound))
    }

    "return NotANumber for everything else" >> {
      val json = Json.Text("miau")

      json.as[Double] must beAFailedTry(equalTo(NotANumber(json)))
    }

  }

  "Decoder[Boolean]" should {

    "decode Json.True" >> {
      val json = Json.True

      json.as[Boolean] must beSuccessfulTry(true)
    }

    "decode Json.False" >> {
      val json = Json.False

      json.as[Boolean] must beSuccessfulTry(false)
    }

    "return NotFound on null" >> {
      val json = Json.Null

      json.as[Boolean] must beAFailedTry(equalTo(NotFound))
    }

    "return NotABoolean for everything else" >> {
      val json = Json.Text("miau")

      json.as[Boolean] must beAFailedTry(equalTo(NotABoolean(json)))
    }

  }

  "Decoder[Option]" should {

    "not fail on Json.Null" >> {
      val json = Json.Null

      json.as[Option[Boolean]] must beSuccessfulTry(Option.empty[Boolean])
    }

    "use A's Decoder in case of non-null" >> {
      val json = Json.True

      json.as[Option[Boolean]] must beSuccessfulTry(some(true))
    }

    "propagate Decoder[A] failure" >> {
      val json = Json.Text("miau")

      json.as[Option[Boolean]] must beAFailedTry(equalTo(NotABoolean(json)))
    }

  }

  "Decoder[List]" should {

    "decode Json.Collection" >> {
      val json = Json.Collection(List(1d, 2d, 3d).map(Json.Number))

      json.as[List[Int]] must beSuccessfulTry(List(1, 2, 3))
    }

    "return NotFound on null" >> {
      val json = Json.Null

      json.as[List[Int]] must beAFailedTry(equalTo(NotFound))
    }

    "return NotAList for everything else" >> {
      val json = Json.Text("miau")

      json.as[List[Int]] must beAFailedTry(equalTo(NotAList(json)))
    }

    "propagate Decoder[A] failure" >> {
      val json = Json.Collection(List("miau").map(Json.Text))

      json.as[List[Int]] must beAFailedTry(equalTo(NotANumber(Json.Text("miau"))))
    }

  }

  "Decoder[ZonedDateTime]" should {

    "decode date time" >> {
      val json = Json.Text("2011-01-26T19:01:12Z")

      val expected = ZonedDateTime.of(2011, 1, 26, 19, 1, 12, 0, UTC)

      json.as[ZonedDateTime] must beSuccessfulTry(expected)
    }

    "return NotFound on null" >> {
      val json = Json.Null

      json.as[ZonedDateTime] must beAFailedTry(equalTo(NotFound))
    }

    "return NotADateTime for texts not containing date times" >> {
      val json = Json.Text("miau")

      json.as[ZonedDateTime] must beAFailedTry(equalTo(NotADateTime(json)))
    }

    "return NotADateTime for everything else" >> {
      val json = Json.True

      json.as[ZonedDateTime] must beAFailedTry(equalTo(NotADateTime(json)))
    }

  }

}
