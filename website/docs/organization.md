---
id: organization
title: Organization
custom_edit_url: https://github.com/alejandrohdezma/sbt-github/edit/master/website/docs/organization.md
---

By default, this plugin will populate `organizationName`, `organizationEmail` and `organizationHomepage` with information from repository's organization. However, there are other scenarios...

## Download owner information if it doesn't have an organization

In case the repository doesn't belong to an organization those settings will be populated with the owner's information. You can disable this functionality by setting `populateOrganizationWithOwner` to `false`:

```scala title="build.sbt"
ThisBuild / populateOrganizationWithOwner := false
```

## Complete override of the organization used

In case the repository's organization is not the one that should be used, in case of repositories under "general" organizations like [sbt](https://github.com/sbt) or companies with multiple Github organizations, you can use the `githubOrganization` setting to completely override the organization information. Just add the following code to your `build.sbt`:

```scala title="build.sbt"
ThisBuild / githubOrganization := "my-organization"
```