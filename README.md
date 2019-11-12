# SBT plugin to read several settings from Github

[![][travis-badge]][travis] [![][maven-badge]][maven] [![][steward-badge]][steward] [![][mergify-badge]][mergify]

This plugin enables several settings automatically by downloaded them from Github:

- `organization`: Retrieved from the current Github user's email (e.g. `info@alejandrohdezma.com` => `com.alejandrohdezma`).
- `homepage`: Retrieved from the Github repository's information.
- `developers`: A list containing the current Github user information.
- `description`: Retrieved from the Github repository.
- `licenses`: Retrieved from the Github repository.

## Installation

Add the following line to your `plugins.sbt` file:

```sbt
addSbtPlugin("com.alejandrohdezma" %% "sbt-me" % "0.2.0")
```

## Configuration

By default, the plugin only downloads the information if an environment variable named `RELEASE` is present in the system SBT is running (the content of the variable is not important). This behaviour can be tweaked by using the `downloadInfoFromGithub` setting:

```sbt
downloadInfoFromGithub := true
```

#### Github API token

In order for this plugin to work you'll need to add an environment variable named `GITHUB_PERSONAL_ACCESS_TOKEN` with a [personal access content](https://github.com/settings/tokens).

[travis]: https://travis-ci.com/alejandrohdezma/sbt-me
[travis-badge]: https://travis-ci.com/alejandrohdezma/sbt-me.svg?branch=master

[maven]: https://search.maven.org/search?q=g:%20com.alejandrohdezma%20AND%20a:sbt-me
[maven-badge]: https://maven-badges.herokuapp.com/maven-central/com.alejandrohdezma/sbt-me/badge.svg?kill_cache=1

[mergify]: https://mergify.io
[mergify-badge]: https://img.shields.io/endpoint.svg?url=https://gh.mergify.io/badges/alejandrohdezma/sbt-me&style=flat

[steward]: https://scala-steward.org
[steward-badge]: https://img.shields.io/badge/Scala_Steward-helping-brightgreen.svg?style=flat&logo=data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAA4AAAAQCAMAAAARSr4IAAAAVFBMVEUAAACHjojlOy5NWlrKzcYRKjGFjIbp293YycuLa3pYY2LSqql4f3pCUFTgSjNodYRmcXUsPD/NTTbjRS+2jomhgnzNc223cGvZS0HaSD0XLjbaSjElhIr+AAAAAXRSTlMAQObYZgAAAHlJREFUCNdNyosOwyAIhWHAQS1Vt7a77/3fcxxdmv0xwmckutAR1nkm4ggbyEcg/wWmlGLDAA3oL50xi6fk5ffZ3E2E3QfZDCcCN2YtbEWZt+Drc6u6rlqv7Uk0LdKqqr5rk2UCRXOk0vmQKGfc94nOJyQjouF9H/wCc9gECEYfONoAAAAASUVORK5CYII=