# Excluding contributors

The `contributors` setting is populated with the information extracted from the repository contributor list. This list will include all Github users who have contributed to the repository, which is not what we always want (including bots, for example). You can exclude certain Github users by using the `excludedContributors` setting.

```scala
ThisBuild / excludedContributors += "my-bot"
```

In addition you can exclude contributors whose Github ID matches some pattern using regex:

```scala
// Will exclude: my-company[bot], external-app[bot]
ThisBuild / excludedContributors += """.*\[bot\]"""
```

By default the following list is excluded:

- scala-steward
- .*[bot]
- traviscibot