---
id: github-token
title: Github Token
custom_edit_url: https://github.com/alejandrohdezma/sbt-github/edit/master/website/docs/github-token.md
---

The Github [personal access token](https://github.com/settings/tokens) that the plugin will use can be set using the `githubAuthToken` setting:

```scala title="build.sbt"
//Defaults to the value of environment variable `GITHUB_TOKEN`
ThisBuild / githubAuthToken := Some(AuthToken("my-github-token"))
```

If you don't want to write your personal token directly in `build.sbt` (which you shouldn't) you can read the value of an environment variable:

```scala title="build.sbt"
Global / githubAuthToken := sys.env.get("GITHUB_TOKEN").map(AuthToken)
```

By default this plugin will look for an environment variable named `GITHUB_TOKEN`.

## Github Actions

If you are using Github Actions, you can use the [provided `GITHUB_TOKEN`](https://help.github.com/en/actions/configuring-and-managing-workflows/authenticating-with-the-github_token#about-the-github_token-secret).
