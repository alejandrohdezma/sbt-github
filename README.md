# SBT plugin to read several settings from Github

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
addSbtPlugin("com.alejandrohdezma" % "sbt-github" % "0.13.0")
```

> If you use [mdoc](https://scalameta.org/mdoc/) there's also available an [mdoc integration module](https://alejandrohdezma.github.io/sbt-github/docs/sbt-mdoc)

> If you use [sbt-header](https://github.com/sbt/sbt-header) there's also available an [sbt-header integration module](https://alejandrohdezma.github.io/sbt-github/docs/sbt-header)

If you want to know more about all the plugin's features, please head on to [its website](https://alejandrohdezma.github.io/sbt-github/) where you will find much more information.

## Contributors for this project

| <a href="https://github.com/alejandrohdezma"><img alt="alejandrohdezma" src="https://avatars.githubusercontent.com/u/9027541?v=4&s=120" width="120px" /></a> | <a href="https://github.com/fedefernandez"><img alt="fedefernandez" src="https://avatars.githubusercontent.com/u/720923?v=4&s=120" width="120px" /></a> |
| :--: | :--: |
| <a href="https://github.com/alejandrohdezma"><sub><b>alejandrohdezma</b></sub></a> | <a href="https://github.com/fedefernandez"><sub><b>fedefernandez</b></sub></a> |