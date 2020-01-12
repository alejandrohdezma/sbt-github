package com.alejandrohdezma.sbt.me.syntax

import com.alejandrohdezma.sbt.me.json.Json
import com.alejandrohdezma.sbt.me.syntax.json._
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

}
