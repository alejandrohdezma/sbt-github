---
layout: docs
title: Use owner as organization
permalink: owner-as-organization
---

# Download owner information if it doesn't have an organization

`sbt-github` will populate `organizationName`, `organizationEmail` and `organizationHomepage` with information from repository's organization. In case the repository doesn't belong to an organization those settings will be populated with the owner's information. You can disable this functionality by setting `populateOrganizationWithOwner` to `false`:

```scala
ThisBuild / populateOrganizationWithOwner := false
```