package com.alejandrohdezma.sbt.github.syntax

import scala.util.{Failure, Try}

object throwable {

  implicit class ThrowableOps(private val throwable: Throwable) extends AnyVal {

    /**
     * Wraps this throwable in a `Try` failure
     */
    def raise[A]: Try[A] = Failure(throwable)

  }

}
