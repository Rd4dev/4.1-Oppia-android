package org.oppia.android.scripts.coverage

import org.oppia.android.scripts.proto.CoverageReport

class CoverageReporter {
  companion object {
    fun generateRichTextReport(report: CoverageReport, format: List<String>) {
      println("Output Format: $format")

      val coverageRatio = computeCoverageRatio(report)
      println("Coverage Ratio: $coverageRatio")
      // generateMarkdown(report)
    }

    fun computeCoverageRatio(coverageReport: CoverageReport) : Double {
      println("From Coverage Reporter: $coverageReport")

      val coveredFile = coverageReport.getCoveredFile(0)
      val linesFound = coveredFile.linesFound
      val linesHit = coveredFile.linesHit

      if (linesFound == 0) {
        return 0.0
      }

      val coverageRatio = (linesHit.toDouble() / linesFound.toDouble()) * 100
      return coverageRatio
    }

    /*fun generateMarkdown(coverageReport: CoverageReport){

    }*/
  }
}
