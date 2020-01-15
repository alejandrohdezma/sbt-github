package com.alejandrohdezma.sbt.me.http

import cats.implicits._

import com.alejandrohdezma.sbt.me.json.Decoder
import com.alejandrohdezma.sbt.me.json.Json.Fail
import com.alejandrohdezma.sbt.me.syntax.json.JsonValueOps
import com.alejandrohdezma.sbt.me.withServer
import org.http4s.dsl.io._
import org.http4s.headers.Authorization
import org.specs2.mutable.Specification

class ClientSpec extends Specification {

  "client.get" should {

    "make `GET` call using auth and returning content as `String`" >> withServer {
      case req @ GET -> Root / "hello" =>
        Ok(s"""{
          "auth": "${req.headers.get(Authorization).map(_.renderString).orEmpty}"
        }""")
    } { uri =>
      final case class Auth(auth: String)

      implicit val decoder: Decoder[Auth] = _.get[String]("auth").map(Auth)
      implicit val auth: Authentication   = Authentication.Token("1234")

      val result = client.get[Auth](s"${uri}hello")

      result must beRight(Auth("Authorization: token 1234"))
    }

    "returns NotFound for unreachable urls" >> withServer {
      case GET -> Root / "hello" => NotFound()
    } { uri =>
      final case class Auth(auth: String)

      val result = client.get[String](s"${uri}hello")

      result must beLeft[Fail](Fail.NotFound)
    }

    "returns Unknown for every other failure (http-related)" >> {
      final case class Auth(auth: String)

      val result = client.get[String]("miau")

      result must beLeft[Fail](Fail.Unknown)
    }

  }

}
