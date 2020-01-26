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
