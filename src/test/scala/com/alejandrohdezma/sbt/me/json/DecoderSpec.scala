package com.alejandrohdezma.sbt.me.json

import com.alejandrohdezma.sbt.me.json.Json.Fail
import com.alejandrohdezma.sbt.me.json.Json.Fail._
import com.alejandrohdezma.sbt.me.syntax.json._
import org.specs2.mutable.Specification

class DecoderSpec extends Specification {

  "Decoder[String]" should {

    "decode Json.Text" >> {
      val json = Json.Text("miau")

      val expected = "miau"

      json.as[String] must beRight(expected)
    }

    "return NotFound on null" >> {
      val json = Json.Null

      json.as[String] must beLeft[Fail](NotFound)
    }

    "return NotAString for everything else" >> {
      val json = Json.Number(42)

      json.as[String] must beLeft[Fail](NotAString(json))
    }

  }

  "Decoder[Long]" should {

    "decode Json.Number" >> {
      val json = Json.Number(42)

      val expected = 42L

      json.as[Long] must beRight(expected)
    }

    "return NotFound on null" >> {
      val json = Json.Null

      json.as[Long] must beLeft[Fail](NotFound)
    }

    "return NotANumber for everything else" >> {
      val json = Json.Text("miau")

      json.as[Long] must beLeft[Fail](NotANumber(json))
    }

  }

  "Decoder[Int]" should {

    "decode Json.Number" >> {
      val json = Json.Number(42)

      val expected = 42

      json.as[Int] must beRight(expected)
    }

    "return NotFound on null" >> {
      val json = Json.Null

      json.as[Int] must beLeft[Fail](NotFound)
    }

    "return NotANumber for everything else" >> {
      val json = Json.Text("miau")

      json.as[Int] must beLeft[Fail](NotANumber(json))
    }

  }

  "Decoder[Double]" should {

    "decode Json.Number" >> {
      val json = Json.Number(42)

      val expected = 42d

      json.as[Double] must beRight(expected)
    }

    "return NotFound on null" >> {
      val json = Json.Null

      json.as[Double] must beLeft[Fail](NotFound)
    }

    "return NotANumber for everything else" >> {
      val json = Json.Text("miau")

      json.as[Double] must beLeft[Fail](NotANumber(json))
    }

  }

  "Decoder[Boolean]" should {

    "decode Json.True" >> {
      val json = Json.True

      json.as[Boolean] must beRight(true)
    }

    "decode Json.False" >> {
      val json = Json.False

      json.as[Boolean] must beRight(false)
    }

    "return NotFound on null" >> {
      val json = Json.Null

      json.as[Boolean] must beLeft[Fail](NotFound)
    }

    "return NotABoolean for everything else" >> {
      val json = Json.Text("miau")

      json.as[Boolean] must beLeft[Fail](NotABoolean(json))
    }

  }

  "Decoder[Option]" should {

    "not fail on Json.Null" >> {
      val json = Json.Null

      json.as[Option[Boolean]] must beRight(Option.empty[Boolean])
    }

    "use A's Decoder in case of non-null" >> {
      val json = Json.True

      json.as[Option[Boolean]] must beRight(some(true))
    }

    "propagate Decoder[A] failure" >> {
      val json = Json.Text("miau")

      json.as[Option[Boolean]] must beLeft[Fail](NotABoolean(json))
    }

  }

}
