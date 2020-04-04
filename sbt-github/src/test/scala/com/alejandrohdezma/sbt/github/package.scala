/*
 * Copyright 2019-2020 Alejandro Hernández <https://github.com/alejandrohdezma>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alejandrohdezma.sbt

import scala.concurrent.ExecutionContext.Implicits.global

import cats.effect.{ContextShift, IO, Timer}

import org.http4s._
import org.http4s.headers.Host
import org.http4s.implicits._
import org.http4s.server.blaze.BlazeServerBuilder

package object github {

  implicit class RequestLinkOps(req: Request[IO]) {

    @SuppressWarnings(Array("scalafix:Disable.get"))
    def urlTo(path: String): String = {
      val host = req.headers.get(Host).get

      s"http://${host.host}:${host.port.getOrElse(8080)}/$path"
    }

  }

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
