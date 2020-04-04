package com.alejandrohdezma.sbt.github.syntax

import scala.util.{Failure, Success, Try}

object scalatry {

  implicit class TryOps[A](private val aTry: Try[A]) extends AnyVal {

    /**
     * The given function is applied if this is a `Failure`.
     * The contained throwable is used as cause for the provided one.
     *
     *  {{{
     *  Failure(NotFound).failMap {
     *    case NotFound => UrlNotFound
     *  } // Result: Failure(UrlNotFound(cause = NotFound))
     *
     *  Failure(NotAString).failMap {
     *    case NotFound => UrlNotFound
     *  } // Result: Failure(NotAString)
     *
     *
     *  Success(12).failMap {
     *    case NotFound => UrlNotFound
     *  } // Result: Success(12)
     *  }}}
     */
    def failMap(pf: PartialFunction[Throwable, Throwable]): Try[A] = aTry match {
      case Success(a) => Success(a)
      case Failure(b) => Failure(pf.andThen(_.initCause(b)).lift(b).getOrElse(b))
    }

    /**
     * Transforms the inner `Throwable` to the provided one if this is a failure.
     * The contained throwable is used as cause for the provided one.
     *
     *  {{{
     *  Failure(NotFound).failAs(UrlNotFound) // Result: Failure(UrlNotFound(cause = NotFound))
     *  Try(12).failAs(NotFound)              // Result: Success(12)
     *  }}}
     */
    def failAs(t: => Throwable): Try[A] = aTry match {
      case Success(a) => Success(a)
      case Failure(b) => Failure(t.initCause(b))
    }

  }

}
