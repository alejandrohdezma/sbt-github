package com.alejandrohdezma.sbt.github.failure

import scala.util.control.NoStackTrace

final case class Path(value: String, fail: Throwable)
    extends Throwable(s"$value => ${fail.getMessage}")
    with NoStackTrace
