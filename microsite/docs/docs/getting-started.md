---
layout: docs
title: Getting Started
permalink: getting-started
---

# Getting started

## Installation

Add the following line to your `plugins.sbt` file:

```scala
addSbtPlugin("com.alejandrohdezma" %% "sbt-github" % "@VERSION@")
```

## Using the plugin

By default, the plugin only downloads the information if an environment variable named `DOWNLOAD_INFO_FROM_GITHUB` is present in the system SBT is running (the content of the variable is not important). This behavior can be tweaked by using the `githubEnabled` setting:

```scala
ThisBuild / githubEnabled := true
```

## Enabled settings

| Setting                | Description                                                                                                                                |
|------------------------|--------------------------------------------------------------------------------------------------------------------------------------------|
| `homepage`             | Retrieved from the Github repository's information.                                                                                        |
| `developers`           | A list containing the repository collaborators.                                                                                            |
| `description`          | Retrieved from the Github repository.                                                                                                      |
| `licenses`             | Retrieved from the Github repository.                                                                                                      |
| `contributors`         | The list of repository contributors. This list will not include contributors set in `excludedContributors`.                                |
| `collaborators`        | The list of repository collaborators who are also `contributors`. This list will always include collaborators set in `extraCollaborators`. |
| `startYear`            | Extracted from the repository creation date.                                                                                               |
| `yearRange`            | A year range that goes from `startYear` to the current year.                                                                               |
| `organizationName`     | The repository organization name or the owner's name if `populateOrganizationWithOwner` is set to `true`.                                  |
| `organizationEmail`    | The repository organization email or the owner's email if `populateOrganizationWithOwner` is set to `true`.                                |
| `organizationHomepage` | The repository organization homepage or the owner's homepage if `populateOrganizationWithOwner` is set to `true`.                          |