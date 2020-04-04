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

package com.alejandrohdezma.sbt.github.syntax

import scala.util.Try

import com.alejandrohdezma.sbt.github.error.NotFound
import com.alejandrohdezma.sbt.github.json.error.{InvalidPath, NotAJSONObject}
import com.alejandrohdezma.sbt.github.json.{Decoder, Json}
import com.alejandrohdezma.sbt.github.syntax.scalatry._
import com.alejandrohdezma.sbt.github.syntax.throwable._

object json {

  implicit class JsonValueOps(private val json: Json.Value) extends AnyVal {

    /** Tries to decode this `Json.Value` as the provided type `A` using its implicit `Decoder` */
    def as[A: Decoder]: Try[A] = Decoder[A].decode(json)

    /**
     * Tries to decode the `Json.Value` at the provided path as the provided type `A` using
     * its implicit `Decoder`.
     *
     * Returns `Failure` with the error in case this is not a `Json.Object` or the decoding fails.
     */
    def get[A: Decoder](path: String): Try[A] = json match {
      case json: Json.Object => json.get(path).as[A].failMap { case t => InvalidPath(path, t) }
      case Json.Null         => NotFound.raise
      case value             => NotAJSONObject(value).raise
    }

  }

  implicit class ResultJsonValueOps(private val result: Try[Json.Value]) extends AnyVal {

    /**
     * If the result is `Right`, tries to decode its `Json.Value` as the provided
     * type `A` using its implicit `Decoder`; otherwise returns the `Result`.
     */
    def as[A: Decoder]: Try[A] = result.flatMap(Decoder[A].decode)

  }

  implicit class JsonObjectOps(private val json: Json.Object) extends AnyVal {

    /** Returns the value for the provided field, if present; otherwise returns `Json.Null` */
    def get(path: String): Json.Value = json.fields.getOrElse(path, Json.Null)

  }

  object / {

    /**
     * `Throwable` extractor:
     * {{{
     *   throwable match {
     *     case "license" / ("url" / NotFound) => ...
     * }}}
     */
    def unapply(throwable: Throwable): Option[(String, Throwable)] = throwable match {
      case InvalidPath(value, fail) => Some(value -> fail)
      case _                        => None
    }
  }

}
