package com.alejandrohdezma.sbt.github.failure

import scala.util.control.NoStackTrace

import com.alejandrohdezma.sbt.github.json.Json

final case class NotANumber(value: Json.Value)
    extends Throwable(s"is not a valid JSON number: $value")
    with NoStackTrace
