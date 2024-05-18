package org.oppia.android.scripts.coverage

import org.oppia.android.scripts.common.BazelClient
import org.oppia.android.scripts.common.CommandExecutor
import org.oppia.android.scripts.common.CommandExecutorImpl
import org.oppia.android.scripts.common.ScriptBackgroundCoroutineDispatcher
import org.oppia.android.scripts.proto.CoverageReport
import org.oppia.android.scripts.proto.CoveredFile
import org.oppia.android.scripts.proto.CoveredLine
import org.oppia.android.scripts.proto.CoveredLine.Coverage
import java.io.File
import java.util.concurrent.TimeUnit
import kotlin.collections.List
import java.util.regex.Matcher
import java.util.regex.Pattern

class CoverageRunner {
  companion object {
    fun runWithCoverageAsync(pathToRoot: String, bazelTestTarget: String) : CoverageReport {
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

        val parsedCoverageReport = parseCoverageData(bazelTestTarget, coverageDataPath)
        // println("Parsed Coverage Report: $parsedCoverageReport")

        return parsedCoverageReport
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

    fun parseCoverageData(bazelTestTarget: String, coverageDataFilePath: String?): CoverageReport {

      // Read and parse the coverage.dat file
      val coverageDataFile = File(coverageDataFilePath)

      /*var currentFilePath: String? = null
      var linesFound = 0
      var linesHit = 0
      var coveredLines = mutableListOf<CoveredLine>()
      val coveredFiles = mutableListOf<CoveredFile>()

      coverageDataFile.forEachLine { line ->
        when {
          line.startsWith("SF:") -> {
            currentFilePath?.let { path ->
              coveredFiles.add(
                CoveredFile.newBuilder()
                  .setFilePath(path)
                  .addAllCoveredLine(coveredLines)
                  .setLinesFound(linesFound)
                  .setLinesHit(linesHit)
                  .build()
              )
            }

            currentFilePath = line.substringAfter("SF:")
            linesFound = 0
            linesHit = 0
            coveredLines = mutableListOf()
          }

          line.startsWith("DA:") -> {
            val parts = line.substringAfter("DA:").split(",")
            val lineNumber = parts[0].toInt()
            val hitCount = parts[1].toInt()
            val coverage = if(hitCount > 0) Coverage.FULL else Coverage.NONE
            coveredLines.add(
              CoveredLine.newBuilder()
                .setLineNumber(lineNumber)
                .setCoverage(coverage)
                .build()
            )
            // manual
            // linesFound++
            // if (hitCount > 0) linesHit++
          }

          // parse LH / LF
          line.startsWith("LH:") -> {
            linesHit = line.substringAfter("LH:").toInt()
          }

          line.startsWith("LF:") -> {
            linesFound = line.substringAfter("LF:").toInt()
          }
        }
      }

      currentFilePath?.let { path ->
        coveredFiles.add(
          CoveredFile.newBuilder()
            .setFilePath(path)
            .addAllCoveredLine(coveredLines)
            .setLinesFound(linesFound)
            .setLinesHit(linesHit)
            .build()
        )
      }

      val coverageReport = CoverageReport.newBuilder()
        .setBazelTestTarget(bazelTestTarget)
        .addAllCoveredFile(coveredFiles)
        .build()

      // println(coverageReport)

      return coverageReport*/


      /*val coverageReportBuilder = CoverageReport.newBuilder()
      println("Coverage Report Builder: $coverageReportBuilder for the coverage data file path: $coverageDataFilePath")
      return coverageReportBuilder.build()*/

      var linesFound = 0
      var linesHit = 0
      val coveredLines = mutableListOf<CoveredLine>()

      coverageDataFile.forEachLine { line ->
        when {
          line.startsWith("DA:") -> {
            val parts = line.substringAfter("DA:").split(",")
            val lineNumber = parts[0].toInt()
            val hitCount = parts[1].toInt()
            val coverage = if (hitCount > 0) Coverage.FULL else Coverage.NONE
            println("Coverage: $coverage")
            coveredLines.add(
              CoveredLine.newBuilder()
                .setLineNumber(lineNumber)
                .setCoverage(coverage)
                .build()
            )
          }

          // parse LH / LF
          line.startsWith("LH:") -> {
            linesHit = line.substringAfter("LH:").toInt()
          }

          line.startsWith("LF:") -> {
            linesFound = line.substringAfter("LF:").toInt()
          }
        }
      }

      val coveredFile = CoveredFile.newBuilder()
        .addAllCoveredLine(coveredLines)
        .setLinesFound(linesFound)
        .setLinesHit(linesHit)
        .build()

      return CoverageReport.newBuilder()
        .setBazelTestTarget(bazelTestTarget)
        .addCoveredFile(coveredFile)
        .build()

    }
  }
}