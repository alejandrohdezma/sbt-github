# Changelog

## [v0.6.0](https://github.com/alejandrohdezma/sbt-github/tree/v0.6.0) (2020-03-01)

[Full Changelog](https://github.com/alejandrohdezma/sbt-github/compare/v0.5.2...v0.6.0)

⚠️ **Breaking changes**

- Use repository's name as `NAME` mdoc variable [\#99](https://github.com/alejandrohdezma/sbt-github/pull/99) ([alejandrohdezma](https://github.com/alejandrohdezma))

🚀 **Features**

- Automatically merge all scala-steward PRs [\#95](https://github.com/alejandrohdezma/sbt-github/pull/95) ([alejandrohdezma](https://github.com/alejandrohdezma))

## [v0.5.2](https://github.com/alejandrohdezma/sbt-github/tree/v0.5.2) (2020-02-17)

[Full Changelog](https://github.com/alejandrohdezma/sbt-github/compare/v0.5.1...v0.5.2)

## [v0.5.1](https://github.com/alejandrohdezma/sbt-github/tree/v0.5.1) (2020-02-14)

[Full Changelog](https://github.com/alejandrohdezma/sbt-github/compare/v0.5.0...v0.5.1)

🚀 **Features**

- Exclude `traviscibot` from contributors by default [\#77](https://github.com/alejandrohdezma/sbt-github/pull/77) ([alejandrohdezma](https://github.com/alejandrohdezma))
- Make `exludedContributors` a list of patterns [\#76](https://github.com/alejandrohdezma/sbt-github/pull/76) ([alejandrohdezma](https://github.com/alejandrohdezma))

## [v0.5.0](https://github.com/alejandrohdezma/sbt-github/tree/v0.5.0) (2020-02-14)

[Full Changelog](https://github.com/alejandrohdezma/sbt-github/compare/v0.4.0...v0.5.0)

🚀 **Features**

- Add `COPYRIGHT\_OWNER` as mdoc variable in sbt-github-mdoc [\#85](https://github.com/alejandrohdezma/sbt-github/pull/85) ([alejandrohdezma](https://github.com/alejandrohdezma))
- Create `sbt-header` integration module [\#49](https://github.com/alejandrohdezma/sbt-github/pull/49) ([alejandrohdezma](https://github.com/alejandrohdezma))
- Make download condition the presence of `DOWNLOAD\_INFO\_FROM\_GITHUB` [\#48](https://github.com/alejandrohdezma/sbt-github/pull/48) ([alejandrohdezma](https://github.com/alejandrohdezma))
- Enable/disable include administrators branch protection before pushing [\#46](https://github.com/alejandrohdezma/sbt-github/pull/46) ([alejandrohdezma](https://github.com/alejandrohdezma))
- Remove pulls & issues urls since they were pointing to API [\#45](https://github.com/alejandrohdezma/sbt-github/pull/45) ([alejandrohdezma](https://github.com/alejandrohdezma))
- Create plugin for removing test dependencies from POM [\#44](https://github.com/alejandrohdezma/sbt-github/pull/44) ([alejandrohdezma](https://github.com/alejandrohdezma))

📘 **Documentation**

- Improve README with GITHUB\_TOKEN tips [\#73](https://github.com/alejandrohdezma/sbt-github/pull/73) ([alejandrohdezma](https://github.com/alejandrohdezma))

🐛 **Bug Fixes**

- Fix broken links, errored versions and descriptions [\#47](https://github.com/alejandrohdezma/sbt-github/pull/47) ([alejandrohdezma](https://github.com/alejandrohdezma))

## [v0.4.0](https://github.com/alejandrohdezma/sbt-github/tree/v0.4.0) (2020-01-26)

[Full Changelog](https://github.com/alejandrohdezma/sbt-github/compare/v0.3.0...v0.4.0)

🚀 **Features**

- Rename plugin to `sbt-github` [\#42](https://github.com/alejandrohdezma/sbt-github/pull/42) ([alejandrohdezma](https://github.com/alejandrohdezma))
- Add `sbt-me-mdoc` plugin [\#41](https://github.com/alejandrohdezma/sbt-github/pull/41) ([alejandrohdezma](https://github.com/alejandrohdezma))
- Recover issues and pulls url from repository endpoint [\#40](https://github.com/alejandrohdezma/sbt-github/pull/40) ([alejandrohdezma](https://github.com/alejandrohdezma))
- Add `scripted` tests [\#39](https://github.com/alejandrohdezma/sbt-github/pull/39) ([alejandrohdezma](https://github.com/alejandrohdezma))
- Make Github entry point URL configurable [\#38](https://github.com/alejandrohdezma/sbt-github/pull/38) ([alejandrohdezma](https://github.com/alejandrohdezma))
- Add logging to operations [\#37](https://github.com/alejandrohdezma/sbt-github/pull/37) ([alejandrohdezma](https://github.com/alejandrohdezma))
- Add simple cache to HTTP client [\#36](https://github.com/alejandrohdezma/sbt-github/pull/36) ([alejandrohdezma](https://github.com/alejandrohdezma))
- Add owner information as organization in case repository doesn't have one [\#35](https://github.com/alejandrohdezma/sbt-github/pull/35) ([alejandrohdezma](https://github.com/alejandrohdezma))
- Retrieve organization email from Github [\#33](https://github.com/alejandrohdezma/sbt-github/pull/33) ([alejandrohdezma](https://github.com/alejandrohdezma))
- Add `yearRange` setting with year range of the project [\#32](https://github.com/alejandrohdezma/sbt-github/pull/32) ([alejandrohdezma](https://github.com/alejandrohdezma))
- Build settings instead of global, replace Travis with github actions [\#31](https://github.com/alejandrohdezma/sbt-github/pull/31) ([alejandrohdezma](https://github.com/alejandrohdezma))
- Add organization information as POM [\#29](https://github.com/alejandrohdezma/sbt-github/pull/29) ([alejandrohdezma](https://github.com/alejandrohdezma))
- Add collaborators informatino as developers to POM [\#28](https://github.com/alejandrohdezma/sbt-github/pull/28) ([alejandrohdezma](https://github.com/alejandrohdezma))
- Add collaborators information [\#27](https://github.com/alejandrohdezma/sbt-github/pull/27) ([alejandrohdezma](https://github.com/alejandrohdezma))
- Add contributors information [\#26](https://github.com/alejandrohdezma/sbt-github/pull/26) ([alejandrohdezma](https://github.com/alejandrohdezma))
- `organization` is mandatory now [\#25](https://github.com/alejandrohdezma/sbt-github/pull/25) ([alejandrohdezma](https://github.com/alejandrohdezma))
- Zero dependencies [\#23](https://github.com/alejandrohdezma/sbt-github/pull/23) ([alejandrohdezma](https://github.com/alejandrohdezma))

🐛 **Bug Fixes**

- Fix scaladocs [\#24](https://github.com/alejandrohdezma/sbt-github/pull/24) ([alejandrohdezma](https://github.com/alejandrohdezma))

## [v0.3.0](https://github.com/alejandrohdezma/sbt-github/tree/v0.3.0) (2019-11-12)

[Full Changelog](https://github.com/alejandrohdezma/sbt-github/compare/v0.2.0...v0.3.0)

## [v0.2.0](https://github.com/alejandrohdezma/sbt-github/tree/v0.2.0) (2019-10-25)

[Full Changelog](https://github.com/alejandrohdezma/sbt-github/compare/v0.1.1...v0.2.0)

## [v0.1.1](https://github.com/alejandrohdezma/sbt-github/tree/v0.1.1) (2019-10-21)

[Full Changelog](https://github.com/alejandrohdezma/sbt-github/compare/v0.1.0...v0.1.1)

## [v0.1.0](https://github.com/alejandrohdezma/sbt-github/tree/v0.1.0) (2019-10-19)

[Full Changelog](https://github.com/alejandrohdezma/sbt-github/compare/f5fed13d09119eb8f3c421baef3226c386f65faa...v0.1.0)



\* *This Changelog was automatically generated by [github_changelog_generator](https://github.com/github-changelog-generator/github-changelog-generator)*
