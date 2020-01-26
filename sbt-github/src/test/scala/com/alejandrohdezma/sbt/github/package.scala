package com.alejandrohdezma.sbt

import scala.concurrent.ExecutionContext.Implicits.global

import cats.effect.{ContextShift, IO, Timer}

import org.http4s._
import org.http4s.implicits._
import org.http4s.server.blaze.BlazeServerBuilder

package object github {

  def withServer[A](pf: PartialFunction[Request[IO], IO[Response[IO]]])(f: String => A): A = {
    implicit val cs: ContextShift[IO] = IO.contextShift(global)
    implicit val timer: Timer[IO]     = IO.timer(global)

    BlazeServerBuilder[IO]
      .withNio2(true)
      .bindAny()
      .withHttpApp(HttpRoutes.of[IO](pf).orNotFound)
      .resource
      .map(_.baseUri.renderString)
      .map(f)
      .use(IO.pure)
      .unsafeRunSync()
  }

}
