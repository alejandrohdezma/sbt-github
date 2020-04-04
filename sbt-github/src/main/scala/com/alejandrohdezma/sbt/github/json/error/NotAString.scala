package com.alejandrohdezma.sbt.github.json.error

import scala.util.control.NoStackTrace

import com.alejandrohdezma.sbt.github.json.Json

final case class NotAString(value: Json.Value)
    extends Throwable(s"$value is not a valid JSON string")
    with NoStackTrace
