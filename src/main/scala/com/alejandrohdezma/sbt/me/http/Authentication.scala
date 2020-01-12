package com.alejandrohdezma.sbt.me.http

trait Authentication {

  def header: String

}

final case class Token(value: String) extends Authentication {

  override def header: String = s"token $value"

}
