package com.alejandrohdezma.sbt.me.http

import java.net.{HttpURLConnection, URL}

import scala.io.Source

object client {

  /**
   * Calls the provided URL with the provided authentication and
   * returns its contents as `String`.
   */
  @SuppressWarnings(Array("all"))
  def get(uri: String)(implicit A: Authentication): String = {
    val url = new URL(s"$uri")

    val connection = url.openConnection.asInstanceOf[HttpURLConnection]

    connection.setRequestProperty("Authorization", A.header)

    val inputStream = connection.getInputStream

    Source.fromInputStream(inputStream, "UTF-8").mkString
  }

}
