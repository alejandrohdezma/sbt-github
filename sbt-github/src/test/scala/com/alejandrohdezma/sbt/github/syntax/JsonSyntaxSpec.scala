package com.alejandrohdezma.sbt.github.syntax

import com.alejandrohdezma.sbt.github.json.Json
import com.alejandrohdezma.sbt.github.json.Json.Fail
import com.alejandrohdezma.sbt.github.json.Json.Fail._
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

      json.get[Boolean]("miau") must beLeft[Fail](NotAJSONObject(json))
    }

    "return NotFound with path if not present" >> {
      val json: Json.Value = Json.Object(Map("miau" -> Json.Number(42d)))

      json.get[Int]("cat") must beLeft[Fail](Path("cat", NotFound))
    }

    "return requested type if present and decoding succeeds" >> {
      val json: Json.Value = Json.Object(Map("miau" -> Json.Number(42d)))

      json.get[Int]("miau") must beRight(42)
    }

    "propagate Decoder[A] failure" >> {
      val json: Json.Value = Json.Object(Map("miau" -> Json.Number(42d)))

      json.get[Boolean]("miau") must beLeft[Fail](Path("miau", NotABoolean(Json.Number(42d))))
    }

  }

  "`/` extractor" should {

    "allow matching path failures" >> {
      val fail: Fail = Path("miau", NotFound)

      fail must be like { case "miau" / NotFound => ok }
    }

    "allow matching nested Path failures" >> {
      val fail: Fail = Path("miau", Path("cat", NotFound))

      fail must be like { case "miau" / ("cat" / NotFound) => ok }
    }

    "not match other failures" >> {
      val fail: Fail = NotFound

      /.unapply(fail) must be none
    }

  }

}
