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

package com.alejandrohdezma.sbt.github.syntax

import com.alejandrohdezma.sbt.github.failure._
import com.alejandrohdezma.sbt.github.json.Decoder.Failure.NotABoolean
import com.alejandrohdezma.sbt.github.json.Json
import com.alejandrohdezma.sbt.github.json.Json.Failures.{NotAJSONObject, Path}
import com.alejandrohdezma.sbt.github.syntax.json._
import org.specs2.mutable.Specification

class JsonSyntaxSpec extends Specification {

  "Json.Object#get" should {

    "return value at path if present" >> {
      val json = Json.Object(Map("miau" -> Json.Number(42d)))

      json.get("miau") must be equalTo Json.Number(42d)
    }

    "return Json.Null if field not present" >> {
      val json = Json.Object(Map("miau" -> Json.Number(42d)))

      json.get("cat") must be equalTo Json.Null
    }

  }

  "Json.value#get" should {

    "return NotAJsonObject if json is not a JSON object" >> {
      val json: Json.Value = Json.True

      json.get[Boolean]("miau") must beLeft[Throwable](NotAJSONObject(json))
    }

    "return NotFound with path if not present" >> {
      val json: Json.Value = Json.Object(Map("miau" -> Json.Number(42d)))

      json.get[Int]("cat") must beLeft[Throwable](Path("cat", NotFound))
    }

    "return requested type if present and decoding succeeds" >> {
      val json: Json.Value = Json.Object(Map("miau" -> Json.Number(42d)))

      json.get[Int]("miau") must beRight(42)
    }

    "propagate Decoder[A] failure" >> {
      val json: Json.Value = Json.Object(Map("miau" -> Json.Number(42d)))

      json.get[Boolean]("miau") must beLeft[Throwable](Path("miau", NotABoolean(Json.Number(42d))))
    }

  }

  "`/` extractor" should {

    "allow matching path failures" >> {
      val fail = Path("miau", NotFound)

      fail must be like { case "miau" / NotFound => ok }
    }

    "allow matching nested Path failures" >> {
      val fail = Path("miau", Path("cat", NotFound))

      fail must be like { case "miau" / ("cat" / NotFound) => ok }
    }

    "not match other failures" >> {
      val fail = NotFound

      /.unapply(fail) must be none
    }

  }

}
