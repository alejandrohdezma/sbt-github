package com.alejandrohdezma.sbt.me.syntax

import com.alejandrohdezma.sbt.me.json.Json.Result
import com.alejandrohdezma.sbt.me.json.{Decoder, Json}

object json {

  implicit class JsonValueOps(private val json: Json.Value) extends AnyVal {

    /** Tries to decode this [[Json.Value]] as the provided type `A` using its implicit [[Decoder]] */
    def as[A: Decoder]: Result[A] = Decoder[A].decode(json)

  }

  implicit class ResultJsonValueOps(private val result: Result[Json.Value]) extends AnyVal {

    /**
     * If the [[Result]] is [[Right]], tries to decode its [[Json.Value]] as the provided
     * type `A` using its implicit [[Decoder]]; otherwise returns the [[Result]].
     */
    def as[A: Decoder]: Result[A] = result.flatMap(Decoder[A].decode)

  }

}
