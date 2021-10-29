/*
 * Copyright 2019-2021 Alejandro Hernández <https://github.com/alejandrohdezma>
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
import scala.util.parsing.combinator.JavaTokenParsers

import org.typelevel.jawn.Facade.SimpleFacade
import org.typelevel.jawn.{Parser => JawnParser}

object Json extends JavaTokenParsers {

  @SuppressWarnings(Array("scalafix:Disable.toString"))
  object JawnFacade extends SimpleFacade[Json.Value] {

    override def jarray(vs: List[Json.Value]): Json.Value = Json.Collection(vs)

    override def jobject(vs: Map[String, Json.Value]): Json.Value = Json.Object(vs)

    override def jnull: Json.Value = Json.Null

    override def jfalse: Json.Value = Json.False

    override def jtrue: Json.Value = Json.True

    override def jnum(s: CharSequence, decIndex: Int, expIndex: Int): Json.Value = Json.Number(s.toString.toDouble)

    override def jstring(s: CharSequence): Json.Value = Json.Text(s.toString)

  }

  def parse(s: String): Try[Json.Value] =
    JawnParser.parseFromString(s)(JawnFacade)

  sealed trait Value

  final case class Object(fields: Map[String, Value]) extends Value

  final case class Collection(elements: List[Value]) extends Value

  final case class Text(value: String) extends Value

  final case class Number(value: Double) extends Value

  case object False extends Value
  case object True  extends Value
  case object Null  extends Value

}
