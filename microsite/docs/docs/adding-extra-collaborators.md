---
layout: docs
title: Adding extra collaborators
permalink: adding-extra-collaborators
---

# Adding extra collaborators

The `collaborators` and `developers` settings are populated with the information extracted from the repository collaborator list (who are also contributors). If you want to include extra collaborators, you can use the `extraCollaborators` setting: 

```scala      
ThisBuild / extraCollaborators += Collaborator(
    "alejandrohdezma",
    "Alejandro Hernández",
    "https://github.com/alejandrohdezma"
)
```

If you don't want to provide all the details for an extra collaborator, you can use the `Collaborator.github` constructor and let `sbt-github` download its information for you:

```scala
ThisBuild / extraCollaborators += Collaborator.github("alejandrohdezma")
```