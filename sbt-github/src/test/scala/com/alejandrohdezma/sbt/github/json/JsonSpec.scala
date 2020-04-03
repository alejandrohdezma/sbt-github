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

import com.alejandrohdezma.sbt.github.json.Json.Fail._
import org.specs2.mutable.Specification

class JsonSpec extends Specification {

  "Json.parse" should {

    "decode Json.Text" >> {
      val json = """"a String""""

      val expected: Json.Value = Json.Text("a String")

      Json.parse(json) must beRight(expected)
    }

    "decode Json.Number" >> {
      val json = "42"

      val expected: Json.Value = Json.Number(42)

      Json.parse(json) must beRight(expected)
    }

    "decode Json.True" >> {
      val json = "true"

      val expected: Json.Value = Json.True

      Json.parse(json) must beRight(expected)
    }

    "decode Json.False" >> {
      val json = "false"

      val expected: Json.Value = Json.False

      Json.parse(json) must beRight(expected)
    }

    "decode Json.Null" >> {
      val json = "null"

      val expected: Json.Value = Json.Null

      Json.parse(json) must beRight(expected)
    }

    "decode Json.Collection" >> {
      val json = "[1, 2, 3, 4]"

      val expected: Json.Value = Json.Collection(List(1d, 2d, 3d, 4d).map(Json.Number))

      Json.parse(json) must beRight(expected)
    }

    "decode Json.Object" >> {
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

      Json.parse(json) must beRight(expected)

    }

  }

  "Fail#msg" should {

    "return readable message for NotAJSONObject failure" >> {
      val fail = NotAJSONObject(Json.Text("miau"))

      fail.msg must be equalTo "is not a valid JSON object: Text(miau)"
    }

    "return readable message for NotAList failure" >> {
      val fail = NotAList(Json.Text("miau"))

      fail.msg must be equalTo "is not a valid JSON array: Text(miau)"
    }

    "return readable message for NotAString failure" >> {
      val fail = NotAString(Json.Number(1d))

      fail.msg must be equalTo "is not a valid JSON string: Number(1.0)"
    }

    "return readable message for NotANumber failure" >> {
      val fail = NotANumber(Json.Text("miau"))

      fail.msg must be equalTo "is not a valid JSON number: Text(miau)"
    }

    "return readable message for NotABoolean failure" >> {
      val fail = NotABoolean(Json.Text("miau"))

      fail.msg must be equalTo "is not a valid JSON boolean: Text(miau)"
    }

    "return readable message for NotADateTime failure" >> {
      val fail = NotADateTime(Json.Text("miau"))

      fail.msg must be equalTo "is not a valid date time: Text(miau)"
    }

    "return readable message for Path failure" >> {
      val fail = Path("miau", NotFound)

      fail.msg must be equalTo "miau => was not found"
    }

    "return readable message for NotAValidJSON failure" >> {
      val fail = NotAValidJSON("miau")

      fail.msg must be equalTo "miau is not a valid JSON"
    }

    "return readable message for Unknown failure" >> {
      val fail = Unknown(new RuntimeException("BOOOM!"))

      fail.msg must be equalTo "An error occurred: BOOOM!"
    }

    "return readable message for URLNotFound failure" >> {
      val fail = URLNotFound("miau")

      fail.msg must be equalTo "miau was not found"
    }

    "return readable message for NotFound failure" >> {
      val fail = NotFound

      fail.msg must be equalTo "was not found"
    }

  }

}
