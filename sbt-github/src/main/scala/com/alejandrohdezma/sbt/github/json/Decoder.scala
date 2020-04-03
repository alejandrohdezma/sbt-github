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

package com.alejandrohdezma.sbt.github.json

import java.time.ZonedDateTime
import java.time.ZonedDateTime.parse

import scala.util.Try

import com.alejandrohdezma.sbt.github.failure.Fail
import com.alejandrohdezma.sbt.github.failure.Fail._
import com.alejandrohdezma.sbt.github.json.Json.Result
import com.alejandrohdezma.sbt.github.syntax.either._
import com.alejandrohdezma.sbt.github.syntax.list._

/**
 * A type class that provides a way to produce a value of type `A` from a [[Json.Value]].
 */
trait Decoder[A] {

  /** Decode the given [[Json.Value]] */
  def decode(json: Json.Value): Result[A]

}

object Decoder {

  def apply[A](implicit D: Decoder[A]): Decoder[A] = D

  def nonNull[A](error: Json.Value => Fail)(f: PartialFunction[Json.Value, A]): Decoder[A] = {
    case Json.Null => Left(NotFound)
    case json      => f.andThen(Right(_)).applyOrElse(json, error.andThen(Left(_)))
  }

  implicit val StringDecoder: Decoder[String] = nonNull(NotAString) {
    case Json.Text(value) => value
  }

  implicit val LongDecoder: Decoder[Long] = nonNull(NotANumber) {
    case Json.Number(value) => value.toLong
  }

  implicit val IntDecoder: Decoder[Int] = nonNull(NotANumber) {
    case Json.Number(value) => value.toInt
  }

  implicit val DoubleDecoder: Decoder[Double] = nonNull(NotANumber) {
    case Json.Number(value) => value
  }

  implicit val BooleanDecoder: Decoder[Boolean] = nonNull(NotABoolean) {
    case Json.True  => true
    case Json.False => false
  }

  implicit val ZonedDateTimeDecoder: Decoder[ZonedDateTime] = {
    case Json.Null            => Left(NotFound)
    case a @ Json.Text(value) => Try(parse(value)).toEither.leftMap(_ => NotADateTime(a))
    case value                => Left(NotADateTime(value))
  }

  implicit def OptionDecoder[A: Decoder]: Decoder[Option[A]] = {
    case Json.Null => Right(None)
    case value     => Decoder[A].decode(value).map(Some(_))
  }

  implicit def ListDecoder[A: Decoder]: Decoder[List[A]] = {
    case Json.Collection(list) => list.traverse(Decoder[A].decode)
    case Json.Null             => Left(NotFound)
    case value                 => Left(NotAList(value))
  }

}
