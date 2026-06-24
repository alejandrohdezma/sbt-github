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

import sbt.librarymanagement.Developer
import sbt.librarymanagement.License
import sbt.librarymanagement.ScmInfo

/** Bridges this plugin's `java.net.URI`-based domain onto the sbt 2.x setting types.
  *
  * sbt 2.x types `homepage`/`organizationHomepage` as `URI`, `licenses` as `Seq[License]` and uses `URI` in `Developer`
  * and `ScmInfo`, so on this axis every conversion is the identity.
  */
object PluginCompat {

  def homepage(uri: URI): URI = uri

  def license(id: String, uri: URI): License = License(id, uri)

  def firstLicenseName(licenses: Seq[License]): Option[String] = licenses.headOption.map(_.spdxId)

  def licenseInfo(licenses: Seq[License]): Seq[(String, String)] =
    licenses.map(license => license.spdxId -> s"${license.uri}")

  def scmInfo(browseUrl: URI, connection: String, devConnection: Option[String]): ScmInfo =
    ScmInfo(browseUrl, connection, devConnection)

  def developer(id: String, name: String, email: String, url: URI): Developer =
    Developer(id, name, email, url)

}
