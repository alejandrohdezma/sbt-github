package com.alejandrohdezma.sbt.me

final class Lazy[T](t: => T) {

  lazy val value: T = t

}

object Lazy {

  def apply[T](t: => T): Lazy[T] = new Lazy[T](t)

}
