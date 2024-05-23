package org.oppia.android.scripts.coverage

import org.oppia.android.scripts.proto.CoverageReport

class CoverageReporter {
  companion object {
    fun generateRichTextReport(report: CoverageReport, format: List<String>) {
      println("Output Format: $format")

      val coverageRatio = computeCoverageRatio(report)
      println("Coverage Ratio: $coverageRatio")

      val mdReport = generateMarkdownReport(report, coverageRatio)
      println("MD Report: $mdReport")

      generateHTMLReport(report, coverageRatio)
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

    fun generateMarkdownReport(coverageReport: CoverageReport, coverageRatio: Double) : String {
      val sb = StringBuilder()
      sb.append("# Coverage Report\n\n")
      sb.append("## Test Target\n")
      sb.append("${coverageReport.bazelTestTarget}\n\n")

      val coveredFile = coverageReport.getCoveredFile(0)
      sb.append("## Covered File\n")
      sb.append("**File Path**: ${coveredFile.filePath}\n\n")
      sb.append("**Lines Found**: ${coveredFile.linesFound}\n\n")
      sb.append("**Lines Hit**: ${coveredFile.linesHit}\n\n")
      sb.append("## Coverage Ratio: ${coverageRatio}%")
      sb.append("\n### Covered Lines\n")
      coveredFile.coveredLineList.forEach { coveredLine ->
        sb.append("  - **Line Number**: ${coveredLine.lineNumber}, **Coverage**: ${coveredLine.coverage}\n")
      }
      sb.append("\n")
      return sb.toString()
    }

    fun generateHTMLReport(coverageReport: CoverageReport, coverageRatio: Double)
  }
}
