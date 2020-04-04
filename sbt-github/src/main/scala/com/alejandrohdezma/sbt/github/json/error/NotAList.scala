package com.alejandrohdezma.sbt.github.json.error

import scala.util.control.NoStackTrace

import com.alejandrohdezma.sbt.github.json.Json

final case class NotAList(value: Json.Value)
    extends Throwable(s"$value is not a valid JSON array")
    with NoStackTrace
