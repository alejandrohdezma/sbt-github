/*
 * Copyright 2019-2026 Alejandro Hernández <https://github.com/alejandrohdezma>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alejandrohdezma.sbt.github

import java.net.URI
import java.net.URL

import sbt.librarymanagement.Developer
import sbt.librarymanagement.ScmInfo

/** Bridges this plugin's `java.net.URI`-based domain onto the sbt 1.x setting types.
  *
  * sbt 1.x types `homepage`/`organizationHomepage` as `URL`, `licenses` as `Seq[(String, URL)]` and uses `URL` in
  * `Developer` and `ScmInfo`, so on this axis each value is converted back with `toURL`.
  */
object PluginCompat {

  def homepage(uri: URI): URL = uri.toURL

  def license(id: String, uri: URI): (String, URL) = id -> uri.toURL

  def firstLicenseName(licenses: Seq[(String, URL)]): Option[String] = licenses.headOption.map(_._1)

  def licenseInfo(licenses: Seq[(String, URL)]): Seq[(String, String)] =
    licenses.map { case (id, url) => id -> s"$url" }

  def scmInfo(browseUrl: URI, connection: String, devConnection: Option[String]): ScmInfo =
    ScmInfo(browseUrl.toURL, connection, devConnection)

  def developer(id: String, name: String, email: String, url: URI): Developer =
    Developer(id, name, email, url.toURL)

}
