# SBT plugin to read several settings from Github

[![][github-action-badge]][github-action] [![][maven-badge]][maven] [![][codecov-badge]][codecov] [![][steward-badge]][steward] 

This plugin enables several settings automatically by downloading them from Github:

- `homepage`: Retrieved from the Github repository's information.
- `developers`: A list containing the repository collaborators.
- `description`: Retrieved from the Github repository.
- `licenses`: Retrieved from the Github repository.
- `contributors`: The list of repository contributors. This list will not include contributors set in `excludedContributors`.
- `collaborators`: The list of repository collaborators who are also `contributors`. This list will always include collaborators set in `extraCollaborators`.
- `startYear`: Extracted from the repository creation date.
- `yearRange`: A year range that goes from `startYear` to the current year.
- `organizationName`: The repository organization name or the owner's name if `populateOrganizationWithOwner` is set to `true`.
- `organizationEmail`: The repository organization email or the owner's email if `populateOrganizationWithOwner` is set to `true`. 
- `organizationHomepage`: The repository organization homepage or the owner's homepage if `populateOrganizationWithOwner` is set to `true`. 

```scala mdoc:toc
```

## Installation

Add the following line to your `plugins.sbt` file:

```sbt
addSbtPlugin("com.alejandrohdezma" %% "sbt-github" % "@VERSION@")
```

> If you use [mdoc](https://scalameta.org/mdoc/) there's also available an [mdoc integration module](#mdoc-integration)

> If you use [sbt-header](https://github.com/sbt/sbt-header) there's also available an [sbt-header integration module](#sbt-header-integration)

## Configuration

By default, the plugin only downloads the information if an environment variable named `DOWNLOAD_INFO_FROM_GITHUB` is present in the system SBT is running (the content of the variable is not important). This behavior can be tweaked by using the `downloadInfoFromGithub` setting:

```sbt
ThisBuild / downloadInfoFromGithub := true
```

### Download owner information if it doesn't have an organization

`sbt-github` will populate `organizationName`, `organizationEmail` and `organizationHomepage` with information from repository's organization. In case the repository doesn't belong to an organization those settings will be populated with the owner's information. You can disable this functionality by setting `populateOrganizationWithOwner` to `false`:

```sbt
ThisBuild / populateOrganizationWithOwner := false
```

### Excluding contributors

The `contributors` setting is populated with the information extracted from the repository contributor list. This list will include all Github users who have contributed to the repository, which is not what we always want (including bots, for example). You can exclude certain Github users by using the `excludedContributors` setting.

```sbt
ThisBuild / excludedContributors += "my-bot"
```

In addition you can exclude contributors whose Github ID matches some pattern using regex:

```sbt
// Will exclude: my-company[bot], external-app[bot]
ThisBuild / excludedContributors += """.*\[bot\]"""
```

By default the following list is excluded:

@EXCLUDED@

### Adding extra collaborators

The `collaborators` and `developers` settings are populated with the information extracted from the repository collaborator list (who are also contributors). If you want to include extra collaborators, you can use the `extraCollaborators` setting: 

```sbt      
ThisBuild / extraCollaborators += Collaborator("alejandrohdezma", "Alejandro Hernández", "https://github.com/alejandrohdezma")
```

If you don't want to provide all the details for an extra collaborator, you can use the `Collaborator.github` constructor and let `sbt-github` download its information for you:

```sbt
ThisBuild / extraCollaborators += Collaborator.github("alejandrohdezma")
```

### Github API token

The Github [personal access token](https://github.com/settings/tokens) that the plugin will use can be set using the `githubToken` setting:

```sbt
//Defaults to the value of environment variable `GITHUB_TOKEN`
ThisBuild / githubToken := Token("my-github-token")
```

If you don't want to write your personal token directly in `build.sbt` (which you shouldn't) you can read the value of an environment variable:

```sbt
Global / githubToken := Token(sys.env.getOrElse("MY_TOKEN", sys.error("Unable to find token")))
```

By default this plugin will look for an environment variable named `GITHUB_TOKEN`.

> If you are using Github Actions, you can use the [provided `GITHUB_TOKEN`](https://help.github.com/en/actions/configuring-and-managing-workflows/authenticating-with-the-github_token#about-the-github_token-secret).

## Integrations

### mdoc integration

If you use [mdoc](https://scalameta.org/mdoc/) for creating your documentation you can benefit from our mdoc module which provides several bunches of [`mdocVariables`](https://scalameta.org/mdoc/docs/installation.html#sbt) already pre-filled with values extracted from Github to any project that adds the `MdocPlugin` to replace them in the documentation. To use it, just add the following line to your `plugins.sbt` file

```sbt
addSbtPlugin("com.alejandrohdezma" %% "sbt-github-mdoc" % "@VERSION@")
```

> Important! So we don't force a version of mdoc, it is requested as a [`Provided`](https://maven.apache.org/guides/introduction/introduction-to-dependency-mechanism.html) dependency so you'll need to provide your own version of mdoc following [its own tutorial](https://scalameta.org/mdoc/docs/installation.html).

The plugin provides the following `mdocVariables`:

| Variable            | Content                                                                                                                                                                    |
|---------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **VERSION**         | Set to the value of the `version` setting by removing the timestamp part (this behavior can be disabled using the `removeVersionTimestampInMdoc` setting)                  |
| **CONTRIBUTORS**    | Set to the value of the `contributors` setting, containing the list of repository contributors in markdown format                                                          |
| **COLLABORATORS**   | Set to the value of the `collaborators` setting, containing the list of repository collaborators in markdown format                                                        |
| **NAME**            | Set to the value of `displayName`. Defaults to repository's name.                                                                                                          |
| **DESCRIPTION**     | Set to the value of `description`                                                                                                                                          |
| **LICENSE**         | Set to the license's name                                                                                                                                                  |
| **ORG_NAME**        | Set to the value of `organizationName` setting (Github's organization name or owner's in case organization is empty and `populateOrganizationWithOwner` is `true`)         |
| **ORG_EMAIL**       | Set to the value of `organizationEmail` setting (Github's organization email, or owner's in case organization is empty and `populateOrganizationWithOwner` is `true`)      |
| **ORG_URL**         | Set to the value of `organizationHomepage` setting (Github's organization homepage or owner's in case organization is empty and `populateOrganizationWithOwner` is `true`) |
| **REPO**            | Set to the repository's path: "owner/repo"                                                                                                                                 |
| **START_YEAR**      | Set to the value of the `startYear` setting                                                                                                                                |
| **YEAR_RANGE**      | Set to the value of the `yearRange` setting                                                                                                                                |
| **COPYRIGHT_OWNER** | Set to the value of `ORG_NAME <ORG_URL>` if `ORG_URL` is present or just `ORG_NAME` in case `ORG_URL` is empty                                                             |

### sbt-header integration

If you use [sbt-header](https://github.com/sbt/sbt-header) for creating/updating your file headers according to your project's license you can benefit from our `sbt-github-header` module which pre-fills header template with downloaded Github values. To use it, just add the following line to your `plugins.sbt` file

```sbt
addSbtPlugin("com.alejandrohdezma" %% "sbt-github-header" % "@VERSION@")
```

> Important! So we don't force a version of sbt-header, it is requested as a [`Provided`](https://maven.apache.org/guides/introduction/introduction-to-dependency-mechanism.html) dependency so you'll need to provide your own version of [sbt-header](https://github.com/sbt/sbt-header).

#### What does this integration do?

Populates the `headerLicense` setting from [`sbt-header`](https://github.com/sbt/sbt-header) with values extracted from Github by `SbtGithubPlugin`:

- **Year**: The information stored in `yearRange`.
- **Copyright Owner**: The information stored in `copyrightOwner`, provided by the own integration
plugin. Defaults to `organizationName` value if there is no value for `organizationHomepage` or
`organizationName <organizationHomepage>` if there is.

The `licenseStyle` setting can be used to tweak the style of the autogenerated headers. Defaults to `Detailed`.

#### Example

For example, given a project with:

 - an @LICENSE@ license...
 - given that the repository start year is @START_YEAR@...
 - that the organization name is @ORG_NAME@...
 - and the organization homepage is @ORG_URL@
 
The following headers will be added to every file:

```scala
/*
 * Copyright (c) @YEAR_RANGE@ @ORG_NAME@ <@ORG_URL@>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
```

[github-action]: https://github.com/alejandrohdezma/sbt-github/actions
[github-action-badge]: https://img.shields.io/endpoint.svg?url=https%3A%2F%2Factions-badge.atrox.dev%2Falejandrohdezma%2Fsbt-github%2Fbadge%3Fref%3Dmaster&style=flat

[maven]: https://search.maven.org/search?q=g:%20com.alejandrohdezma%20AND%20a:sbt-github
[maven-badge]: https://maven-badges.herokuapp.com/maven-central/com.alejandrohdezma/sbt-github/badge.svg?kill_cache=1

[codecov]: https://codecov.io/gh/alejandrohdezma/sbt-github
[codecov-badge]: https://codecov.io/gh/alejandrohdezma/sbt-github/branch/master/graph/badge.svg

[steward]: https://scala-steward.org
[steward-badge]: https://img.shields.io/badge/Scala_Steward-helping-brightgreen.svg?style=flat&logo=data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAA4AAAAQCAMAAAARSr4IAAAAVFBMVEUAAACHjojlOy5NWlrKzcYRKjGFjIbp293YycuLa3pYY2LSqql4f3pCUFTgSjNodYRmcXUsPD/NTTbjRS+2jomhgnzNc223cGvZS0HaSD0XLjbaSjElhIr+AAAAAXRSTlMAQObYZgAAAHlJREFUCNdNyosOwyAIhWHAQS1Vt7a77/3fcxxdmv0xwmckutAR1nkm4ggbyEcg/wWmlGLDAA3oL50xi6fk5ffZ3E2E3QfZDCcCN2YtbEWZt+Drc6u6rlqv7Uk0LdKqqr5rk2UCRXOk0vmQKGfc94nOJyQjouF9H/wCc9gECEYfONoAAAAASUVORK5CYII=
