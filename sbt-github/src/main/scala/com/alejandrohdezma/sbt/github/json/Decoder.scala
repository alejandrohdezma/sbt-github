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

import sbt.URL
import sbt.url

import com.alejandrohdezma.sbt.github.error._
import com.alejandrohdezma.sbt.github.json.error._
import com.alejandrohdezma.sbt.github.syntax.list._
import com.alejandrohdezma.sbt.github.syntax.scalatry._
import com.alejandrohdezma.sbt.github.syntax.throwable._

/**
 * A type class that provides a way to produce a value of type `A` from a [[Json.Value]].
 */
trait Decoder[A] {

  /** Decode the given [[Json.Value]] */
  def decode(json: Json.Value): Try[A]

  /**
   * What should this decoder return if a `Null` is found on the path to getting the value.
   *
   * This doesn't affect when the value itself is `Null`.
   */
  def onNullPath: Try[A] = NotFound.raise

}

object Decoder {

  def apply[A](implicit D: Decoder[A]): Decoder[A] = D

  def nonNull[A](error: Json.Value => Throwable)(f: PartialFunction[Json.Value, A]): Decoder[A] = {
    case Json.Null => NotFound.raise[A]
    case json      => f.andThen(Try(_)).applyOrElse(json, error.andThen(_.raise[A]))
  }

  implicit val StringDecoder: Decoder[String] = nonNull(NotAString) { case Json.Text(value) =>
    value
  }

  implicit val LongDecoder: Decoder[Long] = nonNull(NotANumber) { case Json.Number(value) =>
    value.toLong
  }

  implicit val IntDecoder: Decoder[Int] = nonNull(NotANumber) { case Json.Number(value) =>
    value.toInt
  }

  implicit val DoubleDecoder: Decoder[Double] = nonNull(NotANumber) { case Json.Number(value) =>
    value
  }

  implicit val BooleanDecoder: Decoder[Boolean] = nonNull(NotABoolean) {
    case Json.True  => true
    case Json.False => false
  }

  implicit val ZonedDateTimeDecoder: Decoder[ZonedDateTime] = {
    case Json.Null            => NotFound.raise
    case a @ Json.Text(value) => Try(parse(value)).orElse(NotADateTime(a).raise)
    case value                => NotADateTime(value).raise
  }

  implicit val URLDecoder: Decoder[URL] = {
    case Json.Null            => NotFound.raise
    case v @ Json.Text(value) => Try(url(value)).failAs(NotAUrl(v))
    case value                => NotAUrl(value).raise
  }

  implicit def OptionDecoder[A: Decoder]: Decoder[Option[A]] =
    new Decoder[Option[A]] {

      override def decode(json: Json.Value): Try[Option[A]] =
        json match {
          case Json.Null => Try(None)
          case value     => Decoder[A].decode(value).map(Some(_))
        }

      override def onNullPath: Try[Option[A]] = Try(None)

    }

  implicit def ListDecoder[A: Decoder]: Decoder[List[A]] = {
    case Json.Collection(list) => list.traverse(Decoder[A].decode)
    case Json.Null             => NotFound.raise
    case value                 => NotAList(value).raise
  }

}
