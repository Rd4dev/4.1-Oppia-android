package org.oppia.android.scripts.coverage

import org.oppia.android.scripts.coverage.CoverageRunner

fun main(vararg args: String) {
  val pathToRoot = args[0]
  val targetPath = args[1] // "/app/path/:target"
  val outputFormats = args.slice(2 until args.size).toList()

  RunCoverage().runCoverage(pathToRoot, targetPath, outputFormats)
}

class RunCoverage {
  fun runCoverage(pathToRoot: String, targetPath: String, outputFormats: List<String>) {
    //val coverageReport =
    println("From runcoverage outputFormat: $outputFormats")
    runWithCoverageAnalysis(pathToRoot, targetPath)
  }

  fun runWithCoverageAnalysis(pathToRoot: String, targetPath: String) {
    println("In run with coverage analysis with target Path: $targetPath")
    val coverageAnalysis = CoverageRunner.runWithCoverageAsync(pathToRoot, targetPath)
    println("Coverage Analysis: $coverageAnalysis")
  }
}