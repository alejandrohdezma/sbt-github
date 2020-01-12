package com.alejandrohdezma.sbt.me.http

import java.net.{HttpURLConnection, URL}

import scala.io.Source

import com.alejandrohdezma.sbt.me.json.Json.Result
import com.alejandrohdezma.sbt.me.json.{Decoder, Json}
import com.alejandrohdezma.sbt.me.syntax.json._

object client {

  /**
   * Calls the provided URL with the provided authentication and
   * returns its contents as `String`.
   */
  @SuppressWarnings(Array("all"))
  def get[A: Decoder](uri: String)(implicit A: Authentication): Result[A] = {
    val url = new URL(s"$uri")

    val connection = url.openConnection.asInstanceOf[HttpURLConnection]

    connection.setRequestProperty("Authorization", A.header)

    val inputStream = connection.getInputStream

    val content = Source.fromInputStream(inputStream, "UTF-8").mkString

    Json.parse(content).as[A]
  }

}
