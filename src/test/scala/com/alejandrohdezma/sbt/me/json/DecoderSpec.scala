package com.alejandrohdezma.sbt.me.json

import com.alejandrohdezma.sbt.me.json.Json.Fail
import com.alejandrohdezma.sbt.me.json.Json.Fail._
import org.specs2.mutable.Specification

class DecoderSpec extends Specification {

  "Decoder[String]" should {

    "decode Json.Text" >> {
      val json = Json.Text("miau")

      val expected = "miau"

      Decoder[String].decode(json) must beRight(expected)
    }

    "return NotFound on null" >> {
      val json = Json.Null

      Decoder[String].decode(json) must beLeft[Fail](NotFound)
    }

    "return NotAString for everything else" >> {
      val json = Json.Number(42)

      Decoder[String].decode(json) must beLeft[Fail](NotAString(json))
    }

  }

  "Decoder[Long]" should {

    "decode Json.Number" >> {
      val json = Json.Number(42)

      val expected = 42L

      Decoder[Long].decode(json) must beRight(expected)
    }

    "return NotFound on null" >> {
      val json = Json.Null

      Decoder[Long].decode(json) must beLeft[Fail](NotFound)
    }

    "return NotANumber for everything else" >> {
      val json = Json.Text("miau")

      Decoder[Long].decode(json) must beLeft[Fail](NotANumber(json))
    }

  }

  "Decoder[Int]" should {

    "decode Json.Number" >> {
      val json = Json.Number(42)

      val expected = 42

      Decoder[Int].decode(json) must beRight(expected)
    }

    "return NotFound on null" >> {
      val json = Json.Null

      Decoder[Int].decode(json) must beLeft[Fail](NotFound)
    }

    "return NotANumber for everything else" >> {
      val json = Json.Text("miau")

      Decoder[Int].decode(json) must beLeft[Fail](NotANumber(json))
    }

  }

  "Decoder[Double]" should {

    "decode Json.Number" >> {
      val json = Json.Number(42)

      val expected = 42d

      Decoder[Double].decode(json) must beRight(expected)
    }

    "return NotFound on null" >> {
      val json = Json.Null

      Decoder[Double].decode(json) must beLeft[Fail](NotFound)
    }

    "return NotANumber for everything else" >> {
      val json = Json.Text("miau")

      Decoder[Double].decode(json) must beLeft[Fail](NotANumber(json))
    }

  }

  "Decoder[Boolean]" should {

    "decode Json.True" >> {
      val json = Json.True

      Decoder[Boolean].decode(json) must beRight(true)
    }

    "decode Json.False" >> {
      val json = Json.False

      Decoder[Boolean].decode(json) must beRight(false)
    }

    "return NotFound on null" >> {
      val json = Json.Null

      Decoder[Boolean].decode(json) must beLeft[Fail](NotFound)
    }

    "return NotABoolean for everything else" >> {
      val json = Json.Text("miau")

      Decoder[Boolean].decode(json) must beLeft[Fail](NotABoolean(json))
    }

  }

}
