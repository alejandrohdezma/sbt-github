package com.alejandrohdezma.sbt.github.failure

import scala.util.control.NoStackTrace

final case class Unknown(cause: Throwable)
    extends Throwable(s"An error occurred", cause)
    with NoStackTrace
