package com.alejandrohdezma.sbt.me.syntax

object either {

  implicit class EitherLeftMap[A, B](private val either: Either[A, B]) extends AnyVal {

    /** The given function is applied if this is a `Left`.
     *
     *  {{{
     *  Right(12).map(x => "flower") // Result: Right(12)
     *  Left(12).map(x => "flower")  // Result: Left("flower")
     *  }}}
     */
    def leftMap[C](f: A => C): Either[C, B] = either match {
      case Left(a)  => Left(f(a))
      case Right(b) => Right(b)
    }

  }

}
