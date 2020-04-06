# Changelog

## [Unreleased](https://github.com/alejandrohdezma/sbt-github/tree/HEAD)

[Full Changelog](https://github.com/alejandrohdezma/sbt-github/compare/v0.6.0...HEAD)

🚀 **Features**

- Enable code-coverage retrieval and uploading to Codecov [\#134](https://github.com/alejandrohdezma/sbt-github/pull/134) ([alejandrohdezma](https://github.com/alejandrohdezma))
- Simplify SBT build [\#133](https://github.com/alejandrohdezma/sbt-github/pull/133) ([alejandrohdezma](https://github.com/alejandrohdezma))
- Extract mock server url creation to package object [\#127](https://github.com/alejandrohdezma/sbt-github/pull/127) ([alejandrohdezma](https://github.com/alejandrohdezma))
- Improve documentation on Github Token [\#118](https://github.com/alejandrohdezma/sbt-github/pull/118) ([alejandrohdezma](https://github.com/alejandrohdezma))
- Refactor erroring system [\#117](https://github.com/alejandrohdezma/sbt-github/pull/117) ([alejandrohdezma](https://github.com/alejandrohdezma))

📘 **Documentation**

- Add Codecov badge to README [\#135](https://github.com/alejandrohdezma/sbt-github/pull/135) ([alejandrohdezma](https://github.com/alejandrohdezma))

🐛 **Bug Fixes**

- Fix & Refactor `Collaborator` constructors [\#140](https://github.com/alejandrohdezma/sbt-github/pull/140) ([alejandrohdezma](https://github.com/alejandrohdezma))

📈 **Dependency updates**

- Update http4s to 0.21.3 [\#130](https://github.com/alejandrohdezma/sbt-github/pull/130) ([alejandrohdezma](https://github.com/alejandrohdezma))
- Update sbt-mdoc to 2.1.5 [\#129](https://github.com/alejandrohdezma/sbt-github/pull/129) ([alejandrohdezma](https://github.com/alejandrohdezma))
- Update sbt-mdoc to 2.1.5 [\#124](https://github.com/alejandrohdezma/sbt-github/pull/124) ([scala-steward](https://github.com/scala-steward))
- Update http4s-blaze-server, http4s-dsl to 0.21.3 [\#123](https://github.com/alejandrohdezma/sbt-github/pull/123) ([scala-steward](https://github.com/scala-steward))
- Update sbt to 1.3.9 [\#115](https://github.com/alejandrohdezma/sbt-github/pull/115) ([scala-steward](https://github.com/scala-steward))
- Update http4s-blaze-server, http4s-dsl to 0.21.2 [\#113](https://github.com/alejandrohdezma/sbt-github/pull/113) ([scala-steward](https://github.com/scala-steward))
- Update sbt-mdoc to 2.1.3 [\#111](https://github.com/alejandrohdezma/sbt-github/pull/111) ([scala-steward](https://github.com/scala-steward))
- Update specs2-core to 4.9.2 [\#108](https://github.com/alejandrohdezma/sbt-github/pull/108) ([scala-steward](https://github.com/scala-steward))

## [v0.6.0](https://github.com/alejandrohdezma/sbt-github/tree/v0.6.0) (2020-03-01)

[Full Changelog](https://github.com/alejandrohdezma/sbt-github/compare/v0.5.2...v0.6.0)

⚠️ **Breaking changes**

- Use repository's name as `NAME` mdoc variable [\#99](https://github.com/alejandrohdezma/sbt-github/pull/99) ([alejandrohdezma](https://github.com/alejandrohdezma))

🚀 **Features**

- Automatically merge all scala-steward PRs [\#95](https://github.com/alejandrohdezma/sbt-github/pull/95) ([alejandrohdezma](https://github.com/alejandrohdezma))

📈 **Dependency updates**

- Update specs2-core to 4.9.1 [\#98](https://github.com/alejandrohdezma/sbt-github/pull/98) ([scala-steward](https://github.com/scala-steward))
- Update specs2-core to 4.9.0 [\#97](https://github.com/alejandrohdezma/sbt-github/pull/97) ([scala-steward](https://github.com/scala-steward))
- Update sbt-fix to 0.4.0 [\#96](https://github.com/alejandrohdezma/sbt-github/pull/96) ([scala-steward](https://github.com/scala-steward))
- Update sbt-tpolecat to 0.1.11 [\#90](https://github.com/alejandrohdezma/sbt-github/pull/90) ([scala-steward](https://github.com/scala-steward))
- Update sbt-mdoc-toc to 0.2 [\#89](https://github.com/alejandrohdezma/sbt-github/pull/89) ([scala-steward](https://github.com/scala-steward))

## [v0.5.2](https://github.com/alejandrohdezma/sbt-github/tree/v0.5.2) (2020-02-17)

[Full Changelog](https://github.com/alejandrohdezma/sbt-github/compare/v0.5.1...v0.5.2)

🚀 **Features**

- Add `COPYRIGHT\_OWNER` as mdoc variable in sbt-github-mdoc [\#85](https://github.com/alejandrohdezma/sbt-github/pull/85) ([alejandrohdezma](https://github.com/alejandrohdezma))

## [v0.5.1](https://github.com/alejandrohdezma/sbt-github/tree/v0.5.1) (2020-02-14)

[Full Changelog](https://github.com/alejandrohdezma/sbt-github/compare/v0.5.0...v0.5.1)

🚀 **Features**

- Exclude `traviscibot` from contributors by default [\#77](https://github.com/alejandrohdezma/sbt-github/pull/77) ([alejandrohdezma](https://github.com/alejandrohdezma))
- Make `exludedContributors` a list of patterns [\#76](https://github.com/alejandrohdezma/sbt-github/pull/76) ([alejandrohdezma](https://github.com/alejandrohdezma))

## [v0.5.0](https://github.com/alejandrohdezma/sbt-github/tree/v0.5.0) (2020-02-14)

[Full Changelog](https://github.com/alejandrohdezma/sbt-github/compare/v0.4.0...v0.5.0)

🚀 **Features**

- Create `sbt-header` integration module [\#49](https://github.com/alejandrohdezma/sbt-github/pull/49) ([alejandrohdezma](https://github.com/alejandrohdezma))
- Make download condition the presence of `DOWNLOAD\_INFO\_FROM\_GITHUB` [\#48](https://github.com/alejandrohdezma/sbt-github/pull/48) ([alejandrohdezma](https://github.com/alejandrohdezma))
- Enable/disable include administrators branch protection before pushing [\#46](https://github.com/alejandrohdezma/sbt-github/pull/46) ([alejandrohdezma](https://github.com/alejandrohdezma))
- Remove pulls & issues urls since they were pointing to API [\#45](https://github.com/alejandrohdezma/sbt-github/pull/45) ([alejandrohdezma](https://github.com/alejandrohdezma))
- Create plugin for removing test dependencies from POM [\#44](https://github.com/alejandrohdezma/sbt-github/pull/44) ([alejandrohdezma](https://github.com/alejandrohdezma))

📘 **Documentation**

- Improve README with GITHUB\_TOKEN tips [\#73](https://github.com/alejandrohdezma/sbt-github/pull/73) ([alejandrohdezma](https://github.com/alejandrohdezma))

🐛 **Bug Fixes**

- Fix broken links, errored versions and descriptions [\#47](https://github.com/alejandrohdezma/sbt-github/pull/47) ([alejandrohdezma](https://github.com/alejandrohdezma))

📈 **Dependency updates**

- Update sbt-fix to 0.3.0 [\#84](https://github.com/alejandrohdezma/sbt-github/pull/84) ([scala-steward](https://github.com/scala-steward))
- Update http4s-blaze-server, http4s-dsl to 0.21.1 [\#72](https://github.com/alejandrohdezma/sbt-github/pull/72) ([scala-steward](https://github.com/scala-steward))
- Update http4s-blaze-server, http4s-dsl to 0.21.0 [\#69](https://github.com/alejandrohdezma/sbt-github/pull/69) ([scala-steward](https://github.com/scala-steward))
- Update sbt-ci-release to 1.5.2 [\#68](https://github.com/alejandrohdezma/sbt-github/pull/68) ([scala-steward](https://github.com/scala-steward))
- Update http4s-blaze-server, http4s-dsl to 0.20.17 [\#43](https://github.com/alejandrohdezma/sbt-github/pull/43) ([scala-steward](https://github.com/scala-steward))

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

📈 **Dependency updates**

- Update http4s-blaze-server, http4s-dsl to 0.20.16 [\#34](https://github.com/alejandrohdezma/sbt-github/pull/34) ([scala-steward](https://github.com/scala-steward))
- Update sbt to 1.3.7 [\#30](https://github.com/alejandrohdezma/sbt-github/pull/30) ([scala-steward](https://github.com/scala-steward))
- Update sbt-fix to 0.2.6 [\#22](https://github.com/alejandrohdezma/sbt-github/pull/22) ([scala-steward](https://github.com/scala-steward))
- Update sbt-mdoc to 2.1.1 [\#21](https://github.com/alejandrohdezma/sbt-github/pull/21) ([scala-steward](https://github.com/scala-steward))
- Update sbt-ci-release to 1.5.0 [\#20](https://github.com/alejandrohdezma/sbt-github/pull/20) ([scala-steward](https://github.com/scala-steward))
- Update sbt-mdoc to 2.1.0 [\#19](https://github.com/alejandrohdezma/sbt-github/pull/19) ([scala-steward](https://github.com/scala-steward))
- Update sbt to 1.3.6 [\#18](https://github.com/alejandrohdezma/sbt-github/pull/18) ([scala-steward](https://github.com/scala-steward))
- Update sbt to 1.3.5 [\#17](https://github.com/alejandrohdezma/sbt-github/pull/17) ([scala-steward](https://github.com/scala-steward))
- Update sbt-fix to 0.2.5 [\#16](https://github.com/alejandrohdezma/sbt-github/pull/16) ([scala-steward](https://github.com/scala-steward))
- Update sbt-mdoc to 2.0.3 [\#15](https://github.com/alejandrohdezma/sbt-github/pull/15) ([scala-steward](https://github.com/scala-steward))
- Update sbt-tpolecat to 0.1.10 [\#14](https://github.com/alejandrohdezma/sbt-github/pull/14) ([scala-steward](https://github.com/scala-steward))
- Update sbt-tpolecat to 0.1.9 [\#13](https://github.com/alejandrohdezma/sbt-github/pull/13) ([scala-steward](https://github.com/scala-steward))
- Update sbt to 1.3.4 [\#12](https://github.com/alejandrohdezma/sbt-github/pull/12) ([scala-steward](https://github.com/scala-steward))
- Update sbt-mdoc to 2.0.2 [\#11](https://github.com/alejandrohdezma/sbt-github/pull/11) ([scala-steward](https://github.com/scala-steward))

## [v0.3.0](https://github.com/alejandrohdezma/sbt-github/tree/v0.3.0) (2019-11-12)

[Full Changelog](https://github.com/alejandrohdezma/sbt-github/compare/v0.2.0...v0.3.0)

📈 **Dependency updates**

- Update sbt-mdoc to 2.0.1 [\#10](https://github.com/alejandrohdezma/sbt-github/pull/10) ([scala-steward](https://github.com/scala-steward))
- Update sbt-fix to 0.2.4 [\#9](https://github.com/alejandrohdezma/sbt-github/pull/9) ([scala-steward](https://github.com/scala-steward))
- Update sbt-fix to 0.2.3 [\#8](https://github.com/alejandrohdezma/sbt-github/pull/8) ([scala-steward](https://github.com/scala-steward))
- Update sbt-fix to 0.2.2 [\#7](https://github.com/alejandrohdezma/sbt-github/pull/7) ([scala-steward](https://github.com/scala-steward))
- Update sbt-fix to 0.2.1 [\#6](https://github.com/alejandrohdezma/sbt-github/pull/6) ([scala-steward](https://github.com/scala-steward))
- Update sbt-fix to 0.2.0 [\#5](https://github.com/alejandrohdezma/sbt-github/pull/5) ([scala-steward](https://github.com/scala-steward))

## [v0.2.0](https://github.com/alejandrohdezma/sbt-github/tree/v0.2.0) (2019-10-25)

[Full Changelog](https://github.com/alejandrohdezma/sbt-github/compare/v0.1.1...v0.2.0)

📈 **Dependency updates**

- Update sbt-scalafmt to 2.2.1 [\#4](https://github.com/alejandrohdezma/sbt-github/pull/4) ([scala-steward](https://github.com/scala-steward))
- Update sbt-scalafmt to 2.2.0 [\#3](https://github.com/alejandrohdezma/sbt-github/pull/3) ([scala-steward](https://github.com/scala-steward))
- Update circe-generic, circe-parser to 0.12.3 [\#2](https://github.com/alejandrohdezma/sbt-github/pull/2) ([scala-steward](https://github.com/scala-steward))
- Update scalafmt-core to 2.2.1 [\#1](https://github.com/alejandrohdezma/sbt-github/pull/1) ([scala-steward](https://github.com/scala-steward))

## [v0.1.1](https://github.com/alejandrohdezma/sbt-github/tree/v0.1.1) (2019-10-21)

[Full Changelog](https://github.com/alejandrohdezma/sbt-github/compare/v0.1.0...v0.1.1)

## [v0.1.0](https://github.com/alejandrohdezma/sbt-github/tree/v0.1.0) (2019-10-19)

[Full Changelog](https://github.com/alejandrohdezma/sbt-github/compare/f5fed13d09119eb8f3c421baef3226c386f65faa...v0.1.0)



\* *This Changelog was automatically generated by [github_changelog_generator](https://github.com/github-changelog-generator/github-changelog-generator)*
