import sbt.{Def, _}
import sbt.Keys._
import scoverage.ScoverageSbtPlugin

import sys.process._

/**
 * Companion plugin to `sbt-scoverage` to automatically upload coverage reports
 * to Codecov after successfully running all test configurations.
 *
 * The plugin adds the following tasks/commands:
 *
 *  - '''codecovUpload''': Uploads coverage data to Codecov using the service
 *  recommended method (https://codecov.io/bash).
 *  - '''testCovered''': Runs the test task in all the configurations that enabled
 *  it while recovering coverage data from them. After a successful execution,
 *  uploads the coverage data to Codecov.
 */
object CodeCovPlugin extends AutoPlugin {

  object autoImport {

    val codecovUpload = taskKey[Unit]("Upload code coverage data to Codecov")

  }

  import autoImport._

  override def requires: Plugins = ScoverageSbtPlugin

  override def trigger: PluginTrigger = allRequirements

  override def buildSettings: Seq[Def.Setting[_]] = Seq(
    codecovUpload := {
      val logger = new ProcessLogger {
        override def out(s: => String): Unit = streams.value.log.out(s)

        override def err(s: => String): Unit = streams.value.log.err(s)

        override def buffer[T](f: => T): T = f
      }

      val result = ("bash" #< "curl -s https://codecov.io/bash") ! logger

      if (result != 0) {
        sys.error("Unable to upload coverage to Codecov")
      }
    }
  )

  override def projectSettings: Seq[Def.Setting[_]] = Seq(
    commands += Command.command("testCovered") { state =>
      val testCommand = testConfigs(state).map(_ + ":test").mkString("; ")

      Command.process(
        s"coverageOn; $testCommand; coverageAggregate; codecovUpload; coverageOff",
        state
      )
    }
  )

  /**
   * Returns all the configurations on which the `test` task is enabled.
   */
  private def testConfigs(state: State): Seq[String] = {
    Project
      .extract(state)
      .currentProject
      .settings
      .map(_.key)
      .filter(_.key.label.contentEquals("test"))
      .flatMap(_.scope.config.toOption.map(_.name).toList)
  }

}
