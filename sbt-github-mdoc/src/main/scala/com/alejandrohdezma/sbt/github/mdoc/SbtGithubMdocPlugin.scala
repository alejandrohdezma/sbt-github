package com.alejandrohdezma.sbt.github.mdoc

import sbt.Keys._
import sbt._

import com.alejandrohdezma.sbt.github.SbtGithubPlugin
import com.alejandrohdezma.sbt.github.SbtGithubPlugin.autoImport._
import mdoc.MdocPlugin
import mdoc.MdocPlugin.autoImport.mdocVariables

/**
 * This plugin automatically adds several
 * [[https://scalameta.org/mdoc/docs/installation.html#sbt mdocVariables]]
 * to any project that adds the `MdocPlugin` to replace them in documentation.
 *
 * The list of added variables is:
 *
 *  - '''VERSION''': Set to the value of the `version` setting by removing the timestamp part
 *   (this behavior can be disabled using the `removeVersionTimestampInMdoc` setting).
 *  - '''CONTRIBUTORS''': Set to the value of the `contributors` setting, containing the list
 *   of repository contributors in markdown format.
 *  - '''COLLABORATORS''': Set to the value of the `collaborators` setting, containing the list
 *   of repository collaborators in markdown format.
 *  - '''NAME''': Set to the project's name.
 *  - '''LICENSE''': Set to the license's name.
 *  - '''ORG_NAME''': Set to the value of `organizationName` setting (Github's organization name,
 *   or owner's in case organization is empty and `populateOrganizationWithOwner` is `true`).
 *  - '''ORG_EMAIL''': Set to the value of `organizationEmail` setting (Github's organization email,
 *   or owner's in case organization is empty and `populateOrganizationWithOwner` is `true`).
 *  - '''ORG_URL''': Set to the value of `organizationHomepage` setting (Github's organization homepage,
 *   or owner's in case organization is empty and `populateOrganizationWithOwner` is `true`).
 *  - '''PULLS_URL''': Set to the repository's pull requests url.
 *  - '''ISSUES_URL''': Set to the repository's issues url.
 *  - '''START_YEAR''': Set to the value of the `startYear` setting.
 *  - '''YEAR_RANGE''': Set to the value of the `yearRange` setting
 */
object SbtGithubMdocPlugin extends AutoPlugin {

  object autoImport {

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

  override def projectSettings: Seq[Def.Setting[_]] = Seq(
    removeVersionTimestampInMdoc := true,
    mdocVariables ++= Map(
      "NAME"          -> name.value,
      "LICENSE"       -> licenses.value.headOption.map(_._1).getOrElse(""),
      "ORG_NAME"      -> organizationName.value,
      "ORG_EMAIL"     -> organizationEmail.value.getOrElse(""),
      "ORG_URL"       -> organizationHomepage.value.map(url => s"$url").getOrElse(""),
      "PULLS_URL"     -> repository.value.map(_.pullsUrl).getOrElse(""),
      "ISSUES_URL"    -> repository.value.map(_.issuesUrl).getOrElse(""),
      "START_YEAR"    -> startYear.value.fold("")(year => s"$year"),
      "YEAR_RANGE"    -> yearRange.value.getOrElse(""),
      "VERSION"       -> versionForMdoc.value,
      "CONTRIBUTORS"  -> contributors.value.markdown,
      "COLLABORATORS" -> collaborators.value.markdown
    )
  )

  private val versionForMdoc = Def.setting {
    if (removeVersionTimestampInMdoc.value) version.value.replaceAll("\\+.*", "")
    else version.value
  }

}
