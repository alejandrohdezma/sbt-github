package com.alejandrohdezma.sbt.github.failure

import scala.util.control.NoStackTrace

import com.alejandrohdezma.sbt.github.json.Json

final case class NotAJSONObject(value: Json.Value)
    extends Throwable(s"is not a valid JSON object: $value")
    with NoStackTrace
