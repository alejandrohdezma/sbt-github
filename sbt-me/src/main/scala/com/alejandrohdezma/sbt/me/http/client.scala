package com.alejandrohdezma.sbt.me.http

import java.io.FileNotFoundException
import java.net.{HttpURLConnection, URL}
import java.util.concurrent.ConcurrentHashMap

import scala.io.Source
import scala.util.Try
import scala.util.control.NonFatal

import sbt.util.Logger

import com.alejandrohdezma.sbt.me.json.Json.Fail.URLNotFound
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
  def get[A: Decoder](uri: String)(implicit auth: Authentication, logger: Logger): Result[A] =
    Try {
      logger.verbose(s"Getting content from URL: $uri")

      if (cache.containsKey(uri)) {
        logger.verbose(s"$uri contents already stored on cache")
      }

      cache.computeIfAbsent(
        uri, { _ =>
          val url = new URL(uri)

          logger.verbose(s"Content for $uri not found on cache, downloading...")

          val connection = url.openConnection.asInstanceOf[HttpURLConnection]

          connection.setRequestProperty("Authorization", auth.header)

          val inputStream = connection.getInputStream

          Source.fromInputStream(inputStream, "UTF-8").mkString
        }
      )
    }.toEither.leftMap {
      case _: FileNotFoundException => URLNotFound(uri)
      case NonFatal(t)              => Fail.Unknown(t)
    }.flatMap(Json.parse).as[A]

  private val cache: ConcurrentHashMap[String, String] = new ConcurrentHashMap[String, String]()

}
