package com.alejandrohdezma.sbt.me.syntax

import com.alejandrohdezma.sbt.me.json.Json._
import com.alejandrohdezma.sbt.me.json.{Decoder, Json}
import com.alejandrohdezma.sbt.me.syntax.either._

object json {

  implicit class JsonValueOps(private val json: Json.Value) extends AnyVal {

    /** Tries to decode this [[Json.Value]] as the provided type `A` using its implicit [[Decoder]] */
    def as[A: Decoder]: Result[A] = Decoder[A].decode(json)

    /**
     * Tries to decode the [[Json.Value]] at the provided path as the provided type `A` using
     * its implicit [[Decoder]].
     *
     * Returns [[Left]] with the error in case this is not a [[Json.Object]] or the decoding fails.
     */
    def get[A: Decoder](path: String): Result[A] = json match {
      case json: Json.Object => json.get(path).as[A].leftMap(Fail.Path(path, _))
      case Json.Null         => Left(Fail.NotFound)
      case value             => Left(Fail.NotAJSONObject(value))
    }

  }

  implicit class ResultJsonValueOps(private val result: Result[Json.Value]) extends AnyVal {

    /**
     * If the [[Result]] is [[Right]], tries to decode its [[Json.Value]] as the provided
     * type `A` using its implicit [[Decoder]]; otherwise returns the [[Result]].
     */
    def as[A: Decoder]: Result[A] = result.flatMap(Decoder[A].decode)

  }

  implicit class JsonObjectOps(private val json: Json.Object) extends AnyVal {

    /** Returns the value for the provided field, if present; otherwise returns [[Json.Null]] */
    def get(path: String): Json.Value = json.fields.getOrElse(path, Json.Null)

  }

}
