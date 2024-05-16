package org.oppia.android.scripts.coverage

import org.oppia.android.scripts.common.BazelClient
import org.oppia.android.scripts.common.CommandExecutor
import org.oppia.android.scripts.common.CommandExecutorImpl
import org.oppia.android.scripts.common.ScriptBackgroundCoroutineDispatcher
import java.io.File
import java.util.concurrent.TimeUnit

class CoverageRunner {
  companion object {
    fun runWithCoverageAsync(pathToRoot: String, bazelTestTarget: String) {
      println("In CoverageRunner script - path to root is: $pathToRoot")

      val rootDirectory = File(pathToRoot).absoluteFile

      ScriptBackgroundCoroutineDispatcher().use { scriptBgDispatcher ->
        val commandExecutor: CommandExecutor =
          CommandExecutorImpl(
            scriptBgDispatcher, processTimeout = 5, processTimeoutUnit = TimeUnit.MINUTES
          )

        val bazelClient = BazelClient(rootDirectory, commandExecutor)

        val coverageData = bazelClient.runCoverage(bazelTestTarget)
        println("Coverage Data: $coverageData")
      }
    }
  }
}