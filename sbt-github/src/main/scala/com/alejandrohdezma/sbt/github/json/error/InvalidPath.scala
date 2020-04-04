package com.alejandrohdezma.sbt.github.json.error

import scala.util.control.NoStackTrace

final case class InvalidPath(value: String, fail: Throwable)
    extends Throwable(s"$value => ${fail.getMessage}")
    with NoStackTrace
