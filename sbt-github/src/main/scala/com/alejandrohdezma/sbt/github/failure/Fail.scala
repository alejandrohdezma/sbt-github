package com.alejandrohdezma.sbt.github.failure

import scala.util.control.NoStackTrace

import com.alejandrohdezma.sbt.github.json.Json.Value

object Fail {

  final case class NotAJSONObject(value: Value)
      extends Fail(s"is not a valid JSON object: $value")
      with NoStackTrace

  final case class NotAList(value: Value)
      extends Fail(s"is not a valid JSON array: $value")
      with NoStackTrace

  final case class NotAString(value: Value)
      extends Fail(s"is not a valid JSON string: $value")
      with NoStackTrace

  final case class NotANumber(value: Value)
      extends Fail(s"is not a valid JSON number: $value")
      with NoStackTrace

  final case class NotABoolean(value: Value)
      extends Fail(s"is not a valid JSON boolean: $value")
      with NoStackTrace

  final case class NotADateTime(value: Value)
      extends Fail(s"is not a valid date time: $value")
      with NoStackTrace

  final case class Path(value: String, fail: Fail)
      extends Fail(s"$value => ${fail.getMessage}")
      with NoStackTrace

  final case class NotAValidJSON(string: String)
      extends Fail(s"$string is not a valid JSON")
      with NoStackTrace

  final case class Unknown(cause: Throwable)
      extends Fail(s"An error occurred", cause)
      with NoStackTrace

  final case class URLNotFound(url: String) extends Fail(s"$url was not found") with NoStackTrace

  case object NotFound                      extends Fail("was not found") with NoStackTrace

}
