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

package com.alejandrohdezma.sbt.github.syntax

import scala.util.Failure
import scala.util.Success
import scala.util.Try

object scalatry {

  implicit class TryOps[A](private val aTry: Try[A]) extends AnyVal {

    /** The given function is applied if this is a `Failure`.
      * The contained throwable is used as cause for the provided one.
      *
      *  {{{
      *  Failure(NotFound).collectFail {
      *    case NotFound => UrlNotFound
      *  } // Result: Failure(UrlNotFound(cause = NotFound))
      *
      *  Failure(NotAString).collectFail {
      *    case NotFound => UrlNotFound
      *  } // Result: Failure(NotAString)
      *
      *  Success(12).collectFail {
      *    case NotFound => UrlNotFound
      *  } // Result: Success(12)
      *  }}}
      */
    def collectFail(pf: PartialFunction[Throwable, Throwable]): Try[A] =
      aTry match {
        case Success(a) => Success(a)
        case Failure(b) => Failure(pf.andThen(initCause(b)).lift(b).getOrElse(b))
      }

    /** The given function is applied if this is a `Failure`.
      * The contained throwable is used as cause for the provided one.
      *
      *  {{{
      *  Failure(NotFound).mapFail(_ => UrlNotFound) // Result: Failure(UrlNotFound(cause = NotFound))
      *  Success(12).mapFail(_ => UrlNotFound)       // Result: Success(12)
      *  }}}
      */
    def mapFail(pf: Throwable => Throwable): Try[A] =
      aTry match {
        case Success(a) => Success(a)
        case Failure(b) => Failure(pf.andThen(initCause(b))(b))
      }

    /** Transforms the inner `Throwable` to the provided one if this is a failure.
      * The contained throwable is used as cause for the provided one.
      *
      *  {{{
      *  Failure(NotFound).failAs(UrlNotFound) // Result: Failure(UrlNotFound(cause = NotFound))
      *  Try(12).failAs(NotFound)              // Result: Success(12)
      *  }}}
      */
    def failAs(t: => Throwable): Try[A] =
      aTry match {
        case Success(a) => Success(a)
        case Failure(b) => Failure(initCause(b)(t))
      }

  }

  @SuppressWarnings(Array("all"))
  private def initCause[A](cause: Throwable)(t: Throwable): Throwable =
    if (cause != t && t.getCause == null) t.initCause(cause) else t

}
