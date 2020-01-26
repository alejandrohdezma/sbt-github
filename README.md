# SBT plugin to read several settings from Github

[![][github-action-badge]][github-action] [![][maven-badge]][maven] [![][steward-badge]][steward] [![][mergify-badge]][mergify]

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
  

## Installation

Add the following line to your `plugins.sbt` file:

```sbt
addSbtPlugin("com.alejandrohdezma" %% "sbt-github" % "0.3.0")
```

## Configuration

By default, the plugin only downloads the information if an environment variable named `RELEASE` is present in the system SBT is running (the content of the variable is not important). This behaviour can be tweaked by using the `downloadInfoFromGithub` setting:

```sbt
downloadInfoFromGithub := true
```

### Download owner information if it doesn't have organization

`sbt-github` will populate `organizationName`, `organizationEmail` and `organizationHomepage` with information from repository's organization. In case the repository doesn't belong to an organization those settings will be populated with the owner information. You can disable this functionallity by setting `populateOrganizationWithOwner` to `false`:

```sbt
populateOrganizationWithOwner := false
```

### Excluding contributors

The `contributors` setting is populated with the information extracted from the repository contributor list. This list will include all Github users who have contributed to the repository, which is not what we always want (including bots, for example). You can exclude certain Github users by using the `excludedContributors` setting.

```sbt
excludedContributors += "my-bot"
```

By default the following list is excluded:

- scala-steward
- mergify[bot]

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

In order for this plugin to work you'll need to add an environment variable named `GITHUB_TOKEN` with a [personal access content](https://github.com/settings/tokens).

[github-action]: https://github.com/alejandrohdezma/sbt-github/actions
[github-action-badge]: https://img.shields.io/endpoint.svg?url=https%3A%2F%2Factions-badge.atrox.dev%2Falejandrohdezma%2Fsbt-github%2Fbadge%3Fref%3Dmaster&style=flat

[maven]: https://search.maven.org/search?q=g:%20com.alejandrohdezma%20AND%20a:sbt-github
[maven-badge]: https://maven-badges.herokuapp.com/maven-central/com.alejandrohdezma/sbt-github/badge.svg?kill_cache=1

[mergify]: https://mergify.io
[mergify-badge]: https://img.shields.io/endpoint.svg?url=https://gh.mergify.io/badges/alejandrohdezma/sbt-github&style=flat

[steward]: https://scala-steward.org
[steward-badge]: https://img.shields.io/badge/Scala_Steward-helping-brightgreen.svg?style=flat&logo=data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAA4AAAAQCAMAAAARSr4IAAAAVFBMVEUAAACHjojlOy5NWlrKzcYRKjGFjIbp293YycuLa3pYY2LSqql4f3pCUFTgSjNodYRmcXUsPD/NTTbjRS+2jomhgnzNc223cGvZS0HaSD0XLjbaSjElhIr+AAAAAXRSTlMAQObYZgAAAHlJREFUCNdNyosOwyAIhWHAQS1Vt7a77/3fcxxdmv0xwmckutAR1nkm4ggbyEcg/wWmlGLDAA3oL50xi6fk5ffZ3E2E3QfZDCcCN2YtbEWZt+Drc6u6rlqv7Uk0LdKqqr5rk2UCRXOk0vmQKGfc94nOJyQjouF9H/wCc9gECEYfONoAAAAASUVORK5CYII=
