package com.alejandrohdezma.sbt.github.github.error

import scala.util.control.NoStackTrace

final case class GithubError(msg: String, cause: Throwable)
    extends Throwable(msg, cause)
    with NoStackTrace
