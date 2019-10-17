package com.alejandrohdezma.sbt.me.github

import cats.syntax.either._

import io.circe.CursorOp.{DownField => ⬂}
import io.circe.generic.auto._
import io.circe.parser.decode
import io.circe.{DecodingFailure => Fail}
import sbt.{file, File, IO}
import scalaj.http.Http

object api {

  /**
   * Downloads a remote file from the provided URL and
   * stores in the provided path.
   */
  def copyRemoteConfigFile(uri: String, to: String): File = {
    val localFile = file(to)

    IO.write(localFile, Http(configUrl + uri).asBytes.body)

    localFile
  }

  /** Download repository information from github, or returns a string containing the error */
  def retrieveRepository(name: String, token: String): Either[String, Repository] = {
    val body = Http(s"https://api.github.com/repos/alejandrohdezma/$name")
      .header("Authorization", s"token $token")
      .asString
      .body

    decode[Repository](body).leftMap {
      case Fail(_, List(⬂("description")))           => "Repository doesn't have a description!"
      case Fail(_, List(⬂("spdx_id"), ⬂("license"))) => "Repository doesn't have a license!"
      case Fail(_, List(⬂("url"), ⬂("license")))     => "Repository's license couldn't be inferred!"
      case _                                         => "Unable to get repository information"
    }
  }

  private val configUrl = "https://raw.githubusercontent.com/alejandrohdezma/sbt-me/master/"

}
