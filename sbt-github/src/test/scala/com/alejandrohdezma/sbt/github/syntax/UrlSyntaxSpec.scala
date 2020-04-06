package com.alejandrohdezma.sbt.github.syntax

import sbt.URL

import com.alejandrohdezma.sbt.github.syntax.url._
import org.specs2.mutable.Specification

class UrlSyntaxSpec extends Specification {

  "Uri#withQueryParam" should {

    "add query param to Uri without query" >> {
      val uri = new URL("https://example.com")

      val result = uri.withQueryParam("miau", "42")

      val expected = new URL("https://example.com?miau=42")

      result must be equalTo expected
    }

    "add query param to URL with query" >> {
      val uri = new URL("https://example.com?page=2")

      val result = uri.withQueryParam("miau", "42")

      val expected = new URL("https://example.com?page=2&miau=42")

      result must be equalTo expected
    }

  }

}
