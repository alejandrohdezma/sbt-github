package com.alejandrohdezma.sbt.me.github

import cats.syntax.either._

import com.alejandrohdezma.sbt.me.http._
import io.circe.CursorOp.{DownField => ⬂}
import io.circe.generic.auto._
import io.circe.parser.decode
import io.circe.{DecodingFailure => Fail}

object api {

  /** Download repository information from Github, or returns a string containing the error */
  def retrieveRepository(user: String, name: String)(
      implicit auth: Authentication
  ): Either[String, Repository] = {
    val body = client.get(s"https://api.github.com/repos/$user/$name")

    decode[Repository](body).leftMap {
      case Fail(_, List(⬂("description"))) =>
        s"Repository doesn't have a description! Go to https://github.com/$user/$name and add it"
      case Fail(_, List(⬂("spdx_id"), ⬂("license"))) =>
        s"Repository doesn't have a license! Go to https://github.com/$user/$name and add one"
      case Fail(_, List(⬂("url"), ⬂("license"))) =>
        s"Repository's license couldn't be inferred! Go to https://github.com/$user/$name and check it"
      case _ => "Unable to get repository information"
    }
  }

  /** Download current user information from Github, or returns a string containing the error */
  def retrieveCurrentUser(implicit auth: Authentication): Either[String, CurrentUser] = {
    val body = client.get(s"https://api.github.com/user")

    decode[CurrentUser](body).leftMap {
      case Fail(_, List(⬂("name"))) =>
        "Current user doesn't have a name! Go to https://github.com/settings/profile and add it"
      case Fail(_, List(⬂("email"))) =>
        "Current user doesn't have a name! Go to https://github.com/settings/profile and add it"
      case _ => "Unable to get current user information"
    }
  }

}
