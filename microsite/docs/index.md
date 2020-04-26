---
layout: home
title: Home
section: home
position: 1
---

This plugin enables several settings automatically by downloading them from Github:

| Setting                | Description                                                                                                                                                                                                             |
|------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `homepage`             | Retrieved from the Github repository's information.                                                                                                                                                                     |
| `developers`           | A list containing the repository collaborators.                                                                                                                                                                         |
| `description`          | Retrieved from the Github repository.                                                                                                                                                                                   |
| `licenses`             | Retrieved from the Github repository.                                                                                                                                                                                   |
| `contributors`         | The list of repository contributors. This list will not include contributors set in `excludedContributors`.                                                                                                             |
| `collaborators`        | The list of repository collaborators who are also `contributors`. This list will always include collaborators set in `extraCollaborators`.                                                                              |
| `startYear`            | Extracted from the repository creation date.                                                                                                                                                                            |
| `yearRange`            | A year range that goes from `startYear` to the current year.                                                                                                                                                            |
| `organizationName`     | The repository organization name or the owner's name if `populateOrganizationWithOwner` is set to `true`. This value can be overriden to other organization using [`githubOrganization`](organization).         |
| `organizationEmail`    | The repository organization email or the owner's email if `populateOrganizationWithOwner` is set to `true`. This value can be overriden to other organization using [`githubOrganization`](organization).        |
| `organizationHomepage` | The repository organization homepage or the owner's homepage if `populateOrganizationWithOwner` is set to `true`.  This value can be overriden to other organization using [`githubOrganization`](organization). |

## Installation

Add the following line to your `plugins.sbt` file:

```scala
addSbtPlugin("com.alejandrohdezma" %% "sbt-github" % "@VERSION@")
```

There are some integrations available also:

- If you use [mdoc](https://scalameta.org/mdoc/) there's also available an [mdoc integration module](sbt-mdoc)
- If you use [sbt-header](https://github.com/sbt/sbt-header) there's also available an [sbt-header integration module](sbt-header)
