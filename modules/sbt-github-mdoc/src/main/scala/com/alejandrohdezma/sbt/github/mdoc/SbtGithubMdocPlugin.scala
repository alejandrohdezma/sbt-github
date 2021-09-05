/*
 * Copyright 2019-2021 Alejandro Hernández <https://github.com/alejandrohdezma>
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

package com.alejandrohdezma.sbt.github.mdoc

import sbt.Keys._
import sbt._

import com.alejandrohdezma.sbt.github.SbtGithubPlugin
import com.alejandrohdezma.sbt.github.SbtGithubPlugin.autoImport._
import mdoc.MdocPlugin
import mdoc.MdocPlugin.autoImport.mdocVariables

/** This plugin automatically adds several [[https://scalameta.org/mdoc/docs/installation.html#sbt mdocVariables]] to
  * any project that adds the `MdocPlugin` to replace them in documentation.
  *
  * The list of added variables is:
  *
  *   - '''VERSION''': Set to the value of the `version` setting by removing the timestamp part (this behavior can be
  *     disabled using the `removeVersionTimestampInMdoc` setting).
  *   - '''CONTRIBUTORS''': Set to the value of the `contributors` setting, containing the list of repository
  *     contributors in markdown format.
  *   - '''COLLABORATORS''': Set to the value of the `collaborators` setting, containing the list of repository
  *     collaborators in markdown format.
  *   - '''DESCRIPTION''': Set to the value of the `description` setting.
  *   - '''NAME''': Set to the value of `displayName`. Defaults to repository's name.
  *   - '''LICENSE''': Set to the license's name.
  *   - '''ORG_NAME''': Set to the value of `organizationName` setting (Github's organization name, or owner's in case
  *     organization is empty and `populateOrganizationWithOwner` is `true`).
  *   - '''ORG_EMAIL''': Set to the value of `organizationEmail` setting (Github's organization email, or owner's in
  *     case organization is empty and `populateOrganizationWithOwner` is `true`).
  *   - '''ORG_URL''': Set to the value of `organizationHomepage` setting (Github's organization homepage, or owner's in
  *     case organization is empty and `populateOrganizationWithOwner` is `true`).
  *   - '''REPO''': Set to the repository's path: "owner/repo".
  *   - '''START_YEAR''': Set to the value of the `startYear` setting.
  *   - '''YEAR_RANGE''': Set to the value of the `yearRange` setting
  *   - '''COPYRIGHT_OWNER''': Set to the value of `ORG_NAME <ORG_URL>` if `ORG_URL` is present or just `ORG_NAME` in
  *     case `ORG_URL` is empty.
  */
object SbtGithubMdocPlugin extends AutoPlugin {

  object autoImport {

    val displayName = settingKey[String] {
      "The project name to be used as NAME variable in mdoc, defaults to repository's name"
    }

    val removeVersionTimestampInMdoc = settingKey[Boolean] {
      """Removes the version timestamp set by plugins like 'dwijnand/sbt-dynver':
        |
        |`0.3.0+101-1009de5d+20200116-0827-SNAPSHOT` becomes `0.3.0`
        |
        |this way it can be used to replace version in `README.md`:
        |
        |```sbt
        |libraryDependencies += "com.example" %% "my-library" % "@VERSION@"
        |```
        |
        |Defaults to `true`
        |""".stripMargin
    }

  }

  import autoImport._

  override def trigger: PluginTrigger = allRequirements

  override def requires: Plugins = SbtGithubPlugin && MdocPlugin

  override def projectSettings: Seq[Def.Setting[_]] =
    Seq(
      displayName                  := SbtGithubPlugin.info.value._2,
      removeVersionTimestampInMdoc := true,
      mdocVariables ++= Map(
        "NAME"          -> displayName.value,
        "REPO"          -> repository.value.map(_.name).getOrElse(""),
        "LICENSE"       -> licenses.value.headOption.map(_._1).getOrElse(""),
        "ORG_NAME"      -> organizationName.value,
        "DESCRIPTION"   -> description.value,
        "ORG_EMAIL"     -> organizationEmail.value.getOrElse(""),
        "ORG_URL"       -> organizationHomepage.value.map(url => s"$url").getOrElse(""),
        "START_YEAR"    -> startYear.value.fold("")(year => s"$year"),
        "YEAR_RANGE"    -> yearRange.value.getOrElse(""),
        "VERSION"       -> versionForMdoc.value,
        "CONTRIBUTORS"  -> contributors.value.markdown,
        "COLLABORATORS" -> collaborators.value.markdown,
        "COPYRIGHT_OWNER" -> organizationHomepage.value
          .map(url => s"${organizationName.value} <$url>")
          .getOrElse(organizationName.value)
      )
    )

  private val versionForMdoc = Def.setting {
    if (removeVersionTimestampInMdoc.value) version.value.replaceAll("\\+.*", "")
    else version.value
  }

}
