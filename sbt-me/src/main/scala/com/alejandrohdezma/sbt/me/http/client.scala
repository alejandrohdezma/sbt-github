package com.alejandrohdezma.sbt.me.http

import java.io.FileNotFoundException
import java.net.{HttpURLConnection, URL}
import java.util.concurrent.ConcurrentHashMap

import scala.io.Source
import scala.util.Try
import scala.util.control.NonFatal

import com.alejandrohdezma.sbt.me.json.Json.Fail.NotFound
import com.alejandrohdezma.sbt.me.json.Json.{Fail, Result}
import com.alejandrohdezma.sbt.me.json.{Decoder, Json}
import com.alejandrohdezma.sbt.me.syntax.either._
import com.alejandrohdezma.sbt.me.syntax.json._

object client {

  /**
   * Calls the provided URL with the provided authentication and
   * returns its contents as `String`.
   */
  @SuppressWarnings(Array("all"))
  def get[A: Decoder](uri: String)(implicit A: Authentication): Result[A] =
    Try {
      cache.computeIfAbsent(
        uri, { _ =>
          val url = new URL(s"$uri")

          val connection = url.openConnection.asInstanceOf[HttpURLConnection]

          connection.setRequestProperty("Authorization", A.header)

          val inputStream = connection.getInputStream

          Source.fromInputStream(inputStream, "UTF-8").mkString
        }
      )
    }.toEither.leftMap {
      case _: FileNotFoundException => NotFound
      case NonFatal(_)              => Fail.Unknown
    }.flatMap(Json.parse).as[A]

  private val cache: ConcurrentHashMap[String, String] = new ConcurrentHashMap[String, String]()

}
