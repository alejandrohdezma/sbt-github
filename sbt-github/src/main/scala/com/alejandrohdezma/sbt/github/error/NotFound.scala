package com.alejandrohdezma.sbt.github.error

import scala.util.control.NoStackTrace

case object NotFound extends Throwable("was not found") with NoStackTrace
