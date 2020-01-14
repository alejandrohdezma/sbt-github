package com.alejandrohdezma.sbt.me.github

import com.alejandrohdezma.sbt.me.json.Json
import com.alejandrohdezma.sbt.me.syntax.json._
import org.specs2.mutable.Specification

class UserSpec extends Specification {

  "Decoder[User]" should {

    "treat empty email/name as None" >> {
      val json = Json.parse("""{
        "name": "",
        "email": ""
      }""")

      json.as[User] must beRight(User(None, None))
    }

  }

}
