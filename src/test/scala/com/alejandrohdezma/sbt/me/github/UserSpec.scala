package com.alejandrohdezma.sbt.me.github

import com.alejandrohdezma.sbt.me.json.Json
import com.alejandrohdezma.sbt.me.syntax.json._
import org.specs2.mutable.Specification

class UserSpec extends Specification {

  "Decoder[User]" should {

    "treat empty email/name/avatar as None" >> {
      val json = Json.parse("""{
        "login": "me",
        "html_url": "example.com/me",
        "avatar": "",
        "name": "",
        "email": ""
      }""")

      val expected = User("me", "example.com/me", None, None, None)

      json.as[User] must beRight(expected)
    }

  }

}
