---
id: getting-started
title: Getting Started
slug: /
custom_edit_url: https://github.com/alejandrohdezma/sbt-github/edit/master/website/docs/getting-started.md
---

## Installation

Add the following line to your `plugins.sbt` file:

```scala title="project/plugins.sbt"
addSbtPlugin("com.alejandrohdezma" % "sbt-github" % "@VERSION@")
```

## Using the plugin

By default, the plugin only downloads the information if the `githubEnabled` setting is set to `true`:

```scala title="build.sbt"
ThisBuild / githubEnabled := true
```

If you want to enable this by default on your CI, you can use the aliases `github` or `githubOn`,
for setting it to `true` and `githubOff`, for setting it to `false`.

For example, for enabling downloading information before executing
[`sbt-ci-release`](https://github.com/olafurpg/sbt-ci-release)'s `ci-release` you could do:

```bash
sbt "github; ci-release"
```

## Enabled settings

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
| `organizationName`     | The repository organization name or the owner's name if `populateOrganizationWithOwner` is set to `true`. This value can be overriden to other organization using [`githubOrganization`](organization.md).         |
| `organizationEmail`    | The repository organization email or the owner's email if `populateOrganizationWithOwner` is set to `true`. This value can be overriden to other organization using [`githubOrganization`](organization.md).        |
| `organizationHomepage` | The repository organization homepage or the owner's homepage if `populateOrganizationWithOwner` is set to `true`.  This value can be overriden to other organization using [`githubOrganization`](organization.md). |

There are some integrations available also:

- If you use [mdoc](https://scalameta.org/mdoc/) there's also available an [mdoc integration module](sbt-mdoc.md)
- If you use [sbt-header](https://github.com/sbt/sbt-header) there's also available an [sbt-header integration module](sbt-header.md)