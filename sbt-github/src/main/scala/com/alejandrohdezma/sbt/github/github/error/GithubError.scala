package com.alejandrohdezma.sbt.github.github.error

import scala.util.control.NoStackTrace

final case class GithubError(msg: String) extends Throwable(msg) with NoStackTrace
