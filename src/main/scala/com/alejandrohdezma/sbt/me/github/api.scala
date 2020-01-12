package com.alejandrohdezma.sbt.me.github

import com.alejandrohdezma.sbt.me.http._
import com.alejandrohdezma.sbt.me.json.Json
import com.alejandrohdezma.sbt.me.json.Json.Fail.NotFound
import com.alejandrohdezma.sbt.me.syntax.either._
import com.alejandrohdezma.sbt.me.syntax.json._

object api {

  /** Download repository information from Github, or returns a string containing the error */
  def retrieveRepository(user: String, name: String)(
      implicit auth: Authentication
  ): Either[String, Repository] = {
    val body = client.get(s"https://api.github.com/repos/$user/$name")

    Json.parse(body).as[Repository].leftMap {
      case "description" / NotFound =>
        s"Repository doesn't have a description! Go to https://github.com/$user/$name and add it"
      case "license" / NotFound =>
        s"Repository doesn't have a license! Go to https://github.com/$user/$name and add it"
      case "license" / ("spdx_id" / NotFound) =>
        s"Repository's license id couldn't be inferred! Go to https://github.com/$user/$name and check it"
      case "license" / ("url" / NotFound) =>
        s"Repository's license url couldn't be inferred! Go to https://github.com/$user/$name and check it"
      case _ => "Unable to get repository information"
    }
  }

}
