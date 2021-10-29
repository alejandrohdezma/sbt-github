# SBT plugin to read several settings from Github

[![][maven-badge]][maven] [![][steward-badge]][steward]

This plugin enables several settings automatically by downloading them from Github:

- `homepage`: Retrieved from the Github repository's information.
- `developers`: A list containing the repository collaborators.
- `description`: Retrieved from the Github repository.
- `licenses`: Retrieved from the Github repository.
- `contributors`: The list of repository contributors. This list will not include contributors set in `excludedContributors`.
- `collaborators`: The list of repository collaborators who are also `contributors`. This list will always include collaborators set in `extraCollaborators`.
- `releases`: The list of repository releases.
- `startYear`: Extracted from the repository creation date.
- `yearRange`: A year range that goes from `startYear` to the current year.
- `organizationName`: The repository organization name or the owner's name if `populateOrganizationWithOwner` is set to `true`.
- `organizationEmail`: The repository organization email or the owner's email if `populateOrganizationWithOwner` is set to `true`. 
- `organizationHomepage`: The repository organization homepage or the owner's homepage if `populateOrganizationWithOwner` is set to `true`. 

## Installation

Add the following line to your `plugins.sbt` file:

```sbt
addSbtPlugin("com.alejandrohdezma" % "sbt-github" % "0.11.1")
```

> If you use [mdoc](https://scalameta.org/mdoc/) there's also available an [mdoc integration module](https://alejandrohdezma.github.io/sbt-github/docs/sbt-mdoc)

> If you use [sbt-header](https://github.com/sbt/sbt-header) there's also available an [sbt-header integration module](https://alejandrohdezma.github.io/sbt-github/docs/sbt-header)

If you want to know more about all the plugin's features, please head on to [its website](https://alejandrohdezma.github.io/sbt-github/) where you will find much more information.

## Contributors for this project

| <a href="https://github.com/alejandrohdezma"><img alt="alejandrohdezma" src="https://avatars.githubusercontent.com/u/9027541?v=4&s=120" width="120px" /></a> | <a href="https://github.com/fedefernandez"><img alt="fedefernandez" src="https://avatars.githubusercontent.com/u/720923?v=4&s=120" width="120px" /></a> |
| :--: | :--: |
| <a href="https://github.com/alejandrohdezma"><sub><b>alejandrohdezma</b></sub></a> | <a href="https://github.com/fedefernandez"><sub><b>fedefernandez</b></sub></a> |

[maven]: https://search.maven.org/search?q=g:%20com.alejandrohdezma%20AND%20a:sbt-github
[maven-badge]: https://maven-badges.herokuapp.com/maven-central/com.alejandrohdezma/sbt-github/badge.svg?kill_cache=1

[steward]: https://scala-steward.org
[steward-badge]: https://img.shields.io/badge/Scala_Steward-helping-brightgreen.svg?style=flat&logo=data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAA4AAAAQCAMAAAARSr4IAAAAVFBMVEUAAACHjojlOy5NWlrKzcYRKjGFjIbp293YycuLa3pYY2LSqql4f3pCUFTgSjNodYRmcXUsPD/NTTbjRS+2jomhgnzNc223cGvZS0HaSD0XLjbaSjElhIr+AAAAAXRSTlMAQObYZgAAAHlJREFUCNdNyosOwyAIhWHAQS1Vt7a77/3fcxxdmv0xwmckutAR1nkm4ggbyEcg/wWmlGLDAA3oL50xi6fk5ffZ3E2E3QfZDCcCN2YtbEWZt+Drc6u6rlqv7Uk0LdKqqr5rk2UCRXOk0vmQKGfc94nOJyQjouF9H/wCc9gECEYfONoAAAAASUVORK5CYII=