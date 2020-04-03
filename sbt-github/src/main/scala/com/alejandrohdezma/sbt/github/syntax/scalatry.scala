package com.alejandrohdezma.sbt.github.syntax

import scala.util.{Failure, Success, Try}

object scalatry {

  implicit class TryOps[A](private val aTry: Try[A]) extends AnyVal {

    /** The given function is applied if this is a `Failure`.
     *
     *  {{{
     *  Try {
     *    throw RuntimeException
     *  }.leftMap(x => new IllegalArgumentException(x)) // Result: Failure(IllegalArgumentException)
     *  Try(12).leftMap(x => new IllegalArgumentException(x)) // Result: Success(12)
     *  }}}
     */
    def failMap[C](f: Throwable => Throwable): Try[A] = aTry match {
      case Success(a) => Success(a)
      case Failure(b) => Failure(f(b))
    }

  }

}
