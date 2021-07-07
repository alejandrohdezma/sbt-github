/*
 * Copyright 2019-2021 Alejandro Hernández <https://github.com/alejandrohdezma>
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

import cats.effect.ContextShift
import cats.effect.IO
import cats.effect.Timer

import sbt.URL
import sbt.url
import sbt.util.Logger

import com.alejandrohdezma.sbt.github.github.Collaborator
import com.alejandrohdezma.sbt.github.github.urls.GithubEntryPoint
import com.alejandrohdezma.sbt.github.http.Authentication.AuthToken
import org.http4s._
import org.http4s.headers.Host
import org.http4s.implicits._
import org.http4s.server.blaze.BlazeServerBuilder

package object github {

  @SuppressWarnings(Array("scalafix:DisableSyntax.implicitConversion"))
  implicit def CreatorToCollaborator(creator: Collaborator.Creator): Collaborator =
    creator(AuthToken("123"))(GithubEntryPoint(url"http://example.com"))(
      Logger.Null
    ).get // scalafix:ok Disable.Try.get

  implicit class URLInterpolator(private val sc: StringContext) extends AnyVal {

    def url(args: Any*): URL = sbt.url(sc.raw(args: _*))

  }

  implicit class RequestLinkOps(req: Request[IO]) {

    def urlTo(path: String): String = {
      val host = req.headers.get(Host).get // scalafix:ok Disable.Option.get

      s"http://${host.host}:${host.port.getOrElse(8080)}/$path"
    }

  }

  def withServer[A](pf: PartialFunction[Request[IO], IO[Response[IO]]])(f: URL => A): A = {
    implicit val cs: ContextShift[IO] = IO.contextShift(global)
    implicit val timer: Timer[IO]     = IO.timer(global)

    BlazeServerBuilder[IO](global)
      .bindAny()
      .withHttpApp(HttpRoutes.of[IO](pf).orNotFound)
      .resource
      .map(_.baseUri.renderString)
      .map(url)
      .map(f)
      .use(IO.pure)
      .unsafeRunSync()
  }

}
