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

import scala.util.Try
import scala.util.control.NoStackTrace
import scala.util.parsing.combinator.JavaTokenParsers

import com.alejandrohdezma.sbt.github.syntax.throwable._

object Json extends JavaTokenParsers {

  type Result[A] = Try[A]

  /** Parse the provided string into a [[Json.Value]] */
  def parse(s: String): Result[Json.Value] =
    parseAll(`json-value`, s).map(Try(_)).getOrElse(Failures.NotAValidJSON(s).raise)

  @SuppressWarnings(Array("all"))
  private def `json-value`: Parser[Json.Value] = {
    val stripQuotes   = (x: String) => x.substring(1, x.length - 1)
    val `json-line`   = stringLiteral ~ ":" ~ `json-value`
    val `json-field`  = `json-line` ^^ { case name ~ ":" ~ value => stripQuotes(name) -> value }
    val `json-object` = "{" ~> repsep(`json-field`, ",") <~ "}" ^^ (Map() ++ _) ^^ Json.Object
    val `json-array`  = "[" ~> repsep(`json-value`, ",") <~ "]" ^^ Json.Collection
    val `json-string` = stringLiteral ^^ stripQuotes ^^ Json.Text
    val `json-number` = floatingPointNumber ^^ (_.toDouble) ^^ Json.Number
    val `json-null`   = "null" ^^^ Json.Null
    val `json-true`   = "true" ^^^ Json.True
    val `json-false`  = "false" ^^^ Json.False

    `json-object` | `json-array` | `json-string` | `json-number` | `json-null` | `json-true` | `json-false`
  }

  sealed trait Value

  final case class Object(fields: Map[String, Value]) extends Value
  final case class Collection(elements: List[Value])  extends Value
  final case class Text(value: String)                extends Value
  final case class Number(value: Double)              extends Value
  case object False                                   extends Value
  case object True                                    extends Value
  case object Null                                    extends Value

  object Failures {

    final case class NotAJSONObject(value: Json.Value)
        extends Throwable(s"is not a valid JSON object: $value")
        with NoStackTrace

    final case class NotAValidJSON(string: String)
        extends Throwable(s"$string is not a valid JSON")
        with NoStackTrace

    final case class InvalidPath(value: String, fail: Throwable)
        extends Throwable(s"$value => ${fail.getMessage}")
        with NoStackTrace

  }

}
