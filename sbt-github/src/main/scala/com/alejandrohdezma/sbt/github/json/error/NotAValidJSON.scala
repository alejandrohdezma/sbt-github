package com.alejandrohdezma.sbt.github.json.error

import scala.util.control.NoStackTrace

final case class NotAValidJSON(string: String)
    extends Throwable(s"$string is not a valid JSON")
    with NoStackTrace
