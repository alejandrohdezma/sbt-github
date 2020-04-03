package com.alejandrohdezma.sbt.github.failure

import com.alejandrohdezma.sbt.github.json.Json.Value

/** Represents a generic failure */
abstract class Fail(val msg: String)

object Fail {

  final case class NotAJSONObject(value: Value)    extends Fail(s"is not a valid JSON object: $value")
  final case class NotAList(value: Value)          extends Fail(s"is not a valid JSON array: $value")
  final case class NotAString(value: Value)        extends Fail(s"is not a valid JSON string: $value")
  final case class NotANumber(value: Value)        extends Fail(s"is not a valid JSON number: $value")
  final case class NotABoolean(value: Value)       extends Fail(s"is not a valid JSON boolean: $value")
  final case class NotADateTime(value: Value)      extends Fail(s"is not a valid date time: $value")
  final case class Path(value: String, fail: Fail) extends Fail(s"$value => ${fail.msg}")
  final case class NotAValidJSON(string: String)   extends Fail(s"$string is not a valid JSON")
  final case class Unknown(cause: Throwable)       extends Fail(s"An error occurred: ${cause.getMessage}")
  final case class URLNotFound(url: String)        extends Fail(s"$url was not found")
  case object NotFound                             extends Fail("was not found")

}
