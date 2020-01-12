package com.alejandrohdezma.sbt.me.http

import cats.effect._
import cats.implicits._

import org.http4s._
import org.http4s.dsl.io._
import org.http4s.headers.Authorization
import org.http4s.implicits._
import org.http4s.server.blaze._
import org.specs2.matcher.IOMatchers
import org.specs2.mutable.Specification

class ClientSpec extends Specification with IOMatchers {

  "client.get" should {

    "make `GET` call using auth and returning content as `String`" >> {
      val result = withServer(uri => client.get((uri / "hello").renderString))

      result must returnValue("Authorization: token 1234")
    }

  }

  implicit private val auth: Authentication = Token("1234")

  private def withServer[A](f: Uri => A): IO[A] = {
    val routes = HttpRoutes.of[IO] {
      case req @ GET -> Root / "hello" =>
        Ok(req.headers.get(Authorization).map(_.renderString).orEmpty)
    }

    BlazeServerBuilder[IO]
      .bindAny()
      .withHttpApp(routes.orNotFound)
      .resource
      .map(_.baseUri)
      .map(f)
      .use(IO.pure)
  }

}
