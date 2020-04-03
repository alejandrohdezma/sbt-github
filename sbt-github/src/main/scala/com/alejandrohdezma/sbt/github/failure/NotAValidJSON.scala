package com.alejandrohdezma.sbt.github.failure

import scala.util.control.NoStackTrace

final case class NotAValidJSON(string: String)
    extends Throwable(s"$string is not a valid JSON")
    with NoStackTrace
