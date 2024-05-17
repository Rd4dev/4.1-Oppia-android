package org.oppia.android.scripts.coverage

import org.oppia.android.scripts.common.BazelClient
import org.oppia.android.scripts.common.CommandExecutor
import org.oppia.android.scripts.common.CommandExecutorImpl
import org.oppia.android.scripts.common.ScriptBackgroundCoroutineDispatcher
import java.io.File
import java.util.concurrent.TimeUnit
import kotlin.collections.List
import java.util.regex.Matcher
import java.util.regex.Pattern

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

        val coverageDataPath = extractCoverageDataPath(coverageData)
        println("Coverage Data Path: $coverageDataPath")
      }
    }

    fun extractCoverageDataPath(coverageData: List<String>): String? {
      val regex = ".*coverage\\.dat$".toRegex()
      for (line in coverageData) {
        val match = regex.find(line)
        val extractedPath = match?.value?.substringAfterLast(",")?.trim()
        if (extractedPath != null) {
          return extractedPath
        }
      }
      return null
    }
  }
}