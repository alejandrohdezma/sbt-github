package com.alejandrohdezma.sbt.github.http

trait Authentication {

  def header: String

}

object Authentication {

  final class Token(value: => String) extends Authentication {

    override def header: String = s"token $value"

  }

  object Token {

    def apply(value: => String): Token = new Token(value)

  }

}
