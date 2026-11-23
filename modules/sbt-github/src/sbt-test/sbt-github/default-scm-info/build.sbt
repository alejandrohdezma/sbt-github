import sbtcompat.PluginCompat._

ThisBuild / githubEnabled := false

TaskKey[Unit]("check", "Checks `scmInfo` is inferred from the git remote") := Def.uncached {
  val info = scmInfo.value.getOrElse(sys.error("scmInfo was not inferred"))

  assert(info.browseUrl.toString == "https://github.com/user1/repo")
  assert(info.connection == "scm:git:https://github.com/user1/repo.git")
  assert(info.devConnection.contains("scm:git:git@github.com:user1/repo.git"))
}
