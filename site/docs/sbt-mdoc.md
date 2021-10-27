# Integration with `sbt-mdoc`

If you use [mdoc](https://scalameta.org/mdoc/) for creating your documentation you can benefit from our mdoc module which provides several bunches of [`mdocVariables`](https://scalameta.org/mdoc/docs/installation.html#sbt) already pre-filled with values extracted from Github to any project that adds the `MdocPlugin` to replace them in the documentation. To use it, just add the following line to your `plugins.sbt` file

```scala
addSbtPlugin("com.alejandrohdezma" % "sbt-github-mdoc" % "@VERSION@")
```

!> Important! So we don't force a version of mdoc, it is requested as a ["Provided"](https://maven.apache.org/guides/introduction/introduction-to-dependency-mechanism.html) dependency so you'll need to provide your own version of mdoc following [its own tutorial](https://scalameta.org/mdoc/docs/installation.html).
  
The plugin provides the following `mdocVariables`:

| Variable                | Content                                                                                                                                                                    |
|-------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **VERSION**             | Set to the value of the `version` setting by removing the timestamp part (this behavior can be disabled using the `removeVersionTimestampInMdoc` setting)                  |
| **CONTRIBUTORS**        | Set to the value of the `contributors` setting, containing the list of repository contributors in markdown format                                                          |
| **CONTRIBUTORS_TABLE**  | Set to the value of the `contributors` setting, containing the list of repository contributors as a markdown table                                                         |
| **COLLABORATORS**       | Set to the value of the `collaborators` setting, containing the list of repository collaborators in markdown format                                                        |
| **COLLABORATORS_TABLE** | Set to the value of the `collaborators` setting, containing the list of repository collaborators as a markdown table                                                       |
| **NAME**                | Set to the value of `displayName`. Defaults to repository's name.                                                                                                          |
| **DESCRIPTION**         | Set to the value of `description`                                                                                                                                          |
| **LICENSE**             | Set to the license's name                                                                                                                                                  |
| **ORG_NAME**            | Set to the value of `organizationName` setting (Github's organization name or owner's in case organization is empty and `populateOrganizationWithOwner` is `true`)         |
| **ORG_EMAIL**           | Set to the value of `organizationEmail` setting (Github's organization email, or owner's in case organization is empty and `populateOrganizationWithOwner` is `true`)      |
| **ORG_URL**             | Set to the value of `organizationHomepage` setting (Github's organization homepage or owner's in case organization is empty and `populateOrganizationWithOwner` is `true`) |
| **REPO**                | Set to the repository's path: "owner/repo"                                                                                                                                 |
| **START_YEAR**          | Set to the value of the `startYear` setting                                                                                                                                |
| **YEAR_RANGE**          | Set to the value of the `yearRange` setting                                                                                                                                |
| **COPYRIGHT_OWNER**     | Set to the value of `ORG_NAME ` if `ORG_URL` is present or just `ORG_NAME` in case `ORG_URL` is empty                                                                      |
