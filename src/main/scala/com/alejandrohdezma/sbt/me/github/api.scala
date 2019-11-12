package com.alejandrohdezma.sbt.me.github

import cats.syntax.either._

import io.circe.CursorOp.{DownField => ⬂}
import io.circe.generic.auto._
import io.circe.parser.decode
import io.circe.{DecodingFailure => Fail}
import scalaj.http.Http

object api {

  /** Represents the Github API token used to authenticate to the Github API */
  final case class GithubToken(value: String) extends AnyVal

  /** Download repository information from github, or returns a string containing the error */
  def retrieveRepository(user: String, name: String)(
      implicit token: GithubToken
  ): Either[String, Repository] = {
    val body = Http(s"https://api.github.com/repos/$user/$name")
      .header("Authorization", s"token ${token.value}")
      .asString
      .body

    decode[Repository](body).leftMap {
      case Fail(_, List(⬂("description")))           => "Repository doesn't have a description!"
      case Fail(_, List(⬂("spdx_id"), ⬂("license"))) => "Repository doesn't have a license!"
      case Fail(_, List(⬂("url"), ⬂("license")))     => "Repository's license couldn't be inferred!"
      case _                                         => "Unable to get repository information"
    }
  }

}
