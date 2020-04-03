package com.alejandrohdezma.sbt.github.failure

import scala.util.control.NoStackTrace

import com.alejandrohdezma.sbt.github.json.Json

final case class NotAList(value: Json.Value)
    extends Throwable(s"is not a valid JSON array: $value")
    with NoStackTrace
