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

object either {

  implicit class EitherLeftMap[A, B](private val either: Either[A, B]) extends AnyVal {

    /** The given function is applied if this is a `Left`.
     *
     *  {{{
     *  Right(12).leftMap(x => "flower") // Result: Right(12)
     *  Left(12).leftMap(x => "flower")  // Result: Left("flower")
     *  }}}
     */
    def leftMap[C](f: A => C): Either[C, B] = either match {
      case Left(a)  => Left(f(a))
      case Right(b) => Right(b)
    }

    /** The given consumer is applied if this is a `Left`.
     *
     *  {{{
     *  Right(12).onLeft(println) // Result: Right(12), nothing is printed
     *  Left(12).onLeft(println)  // Result: "12" gets printed
     *  }}}
     */
    def onLeft[C](f: A => Unit): Either[A, B] = either match {
      case l @ Left(a) => {
        f(a)
        l
      }
      case r => r
    }

  }

}
