package org.msse696.optimization.helper.report;

import org.msse696.automation.InefficiencyAnalyzerRunner;
import org.msse696.optimization.helper.JavaFileScanner;
import org.msse696.optimization.helper.debug.Debug;

import java.util.List;

/**
 * Generates a combined HTML report summarizing and detailing code inefficiencies.
 */
public class CombinedAnalysisReportGenerator {

    /**
     * Generates the combined HTML report.
     *
     * @param runners      A list of InefficiencyAnalyzerRunner objects, each holding analysis results for a file.
     * @param reportPath   The file path where the HTML report will be saved.
     * @param createReport A boolean indicating whether to generate the report.
     */
    public static void generateReport(List<InefficiencyAnalyzerRunner> runners, String reportPath, boolean createReport) {
        int totalOverallInefficiencies = 0;
        int totalFilesAnalyzed = 0;
        StringBuilder reportContent = new StringBuilder();

        if (createReport) {
            reportContent.append("<h2>Analysis Summary:</h2>\n");
            reportContent.append("<ul>\n");
        }

        for (InefficiencyAnalyzerRunner runner : runners) {
            totalFilesAnalyzed++;
            String className = runner.getJavaFile().getName();
            String[][] recommendations = runner.getRecommendedData(); // Get recommendations

            // If recommendations exist, add to summary with a link
            if (recommendations != null && recommendations.length > 0) {
                totalOverallInefficiencies += runner.getTotalInefficiencyCount();
                if (createReport) {
                    String linkId = className.replace(".java", "").toLowerCase() + "-report";
                    reportContent.append("<li><a href='#").append(linkId).append("'>").append(className).append("</a> (").append(runner.getTotalInefficiencyCount()).append(" inefficiencies)</li>\n");
                }
            } else {
                // If no recommendations, indicate no inefficiencies
                if (createReport) {
                    reportContent.append("<li>").append(className).append(" (No inefficiencies detected)</li>\n");
                }
            }
        }

        if (createReport) {
            reportContent.append("</ul>\n");
            reportContent.append("<p><strong>Total Inefficiencies Detected Across All Classes: ").append(totalOverallInefficiencies).append("</strong></p>\n");
            JavaFileScanner scanner = new JavaFileScanner(reportPath + "/..");
            int totalLinesOfCode = scanner.getTotalNumberOfLines();
            reportContent.append("<p><strong>Total # of lines of code Across All Classes: ").append(totalLinesOfCode).append("</strong></p>\n");
            reportContent.append("<p><strong>Total Java Files Analyzed: ").append(totalFilesAnalyzed).append("</strong></p>\n");
            reportContent.append("<hr>\n");
            reportContent.append("<h2>Detailed Reports by Class:</h2>\n");

            for (InefficiencyAnalyzerRunner runner : runners) {
                String[][] recommendations = runner.getRecommendedData(); // Get recommendations again
                // If recommendations exist, generate a detailed report section
                if (recommendations != null && recommendations.length > 0) {
                    String className = runner.getJavaFile().getName();
                    String reportTitle = "Inefficiency Report for " + className;
                    String linkId = className.replace(".java", "").toLowerCase() + "-report";
                    reportContent.append("<h3 id='").append(linkId).append("'>").append(reportTitle).append("</h3>\n");
                    reportContent.append("<table style='width:100%; border-collapse: collapse;'>\n");
                    reportContent.append("<tr><th style='border: 1px solid #ddd; padding: 8px; text-align: left;'>Analyzer</th><th style='border: 1px solid #ddd; padding: 8px; text-align: left;'>Inefficient Code</th><th style='border: 1px solid #ddd; padding: 8px; text-align: left;'>Efficient Code</th></tr>\n");
                    for (String[] recommendation : recommendations) {
                        reportContent.append("<tr style='border: 1px solid #ddd;'>");
                        for (int i = 0; i < recommendation.length; i++) {
                            reportContent.append("<td style='border: 1px solid #ddd; padding: 8px;'>");
                            if (i > 0) {
                                reportContent.append("<pre><code style='white-space: pre-wrap;'>").append(escapeHtml(recommendation[i])).append("</code></pre>");
                            } else {
                                reportContent.append(escapeHtml(recommendation[i]));
                            }
                            reportContent.append("</td>");
                        }
                        if (recommendation.length == 2) {
                            reportContent.append("<td style='border: 1px solid #ddd; padding: 8px;'>N/A</td>");
                        }
                        reportContent.append("</tr>\n");
                    }
                    reportContent.append("</table>\n");
                    reportContent.append("<hr>\n");
                } else {
                    Debug.info("No specific recommendations found for class: " + runner.getJavaFile().getName());
                }
            }

            HtmlReport.generateHtmlReport("Inefficiency Analysis Report", reportContent.toString(), reportPath);
            Debug.info("Combined analysis report generated at: " + reportPath);
        } else {
            Debug.info("Analysis complete. Total files analyzed: " + totalFilesAnalyzed + ", Total inefficiencies found: " + totalOverallInefficiencies + ". Report generation skipped.");
        }
    }

    /**
     * Escapes HTML special characters to prevent rendering issues.
     *
     * @param text The text to escape.
     * @return The escaped HTML text.
     */
    private static String escapeHtml(String text) {
        return (text == null) ? "" : text.replace("<", "&lt;").replace(">", "&gt;");
    }
}