package com.alejandrohdezma.sbt.github.failure

import scala.util.control.NoStackTrace

final case class URLNotFound(url: String) extends Throwable(s"$url was not found") with NoStackTrace
