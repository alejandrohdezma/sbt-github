package com.alejandrohdezma.sbt.github.syntax

import sbt.URI
import sbt.URL

object url {

  implicit class UrlOps(private val url: URL) extends AnyVal {

    /**
     * Adds a query param with the given `key`/`value` pair to
     * this `URL` ad returns it.
     */
    @SuppressWarnings(Array("scalafix:Disable.toURI"))
    def withQueryParam(key: String, value: String): URL = {
      val uri = url.toURI

      val query = Option(uri.getQuery)
        .map(_ + s"&$key=$value")
        .getOrElse(s"$key=$value")

      new URI(
        uri.getScheme,
        uri.getAuthority,
        uri.getPath,
        query,
        uri.getFragment
      ).toURL
    }

  }

}
