package org.msse696.optimization.helper.report;

import org.msse696.automation.InefficiencyAnalyzerRunner;
import org.msse696.optimization.helper.debug.Debug;
import org.msse696.optimization.helper.report.HtmlReport;

import java.util.List;

public class CombinedAnalysisReportGenerator {

    public static void generateReport(List<InefficiencyAnalyzerRunner> runners, String reportPath) {
        StringBuilder reportContent = new StringBuilder();
        int totalOverallInefficiencies = 0;

        reportContent.append("<h2>Analysis Summary:</h2>\n");
        reportContent.append("<ul>\n");
        for (InefficiencyAnalyzerRunner runner : runners) {
            String className = runner.getJavaFile().getName();
            if (!runner.isEfficient()) {
                String linkId = className.replace(".java", "").toLowerCase() + "-report";
                reportContent.append("<li><a href='#").append(linkId).append("'>").append(className).append("</a> (").append(runner.getTotalInefficiencyCount()).append(" inefficiencies)</li>\n");
                totalOverallInefficiencies += runner.getTotalInefficiencyCount();
            } else {
                reportContent.append("<li>").append(className).append(" (No inefficiencies detected)</li>\n");
            }
        }
        reportContent.append("</ul>\n");

        reportContent.append("<p><strong>Total Inefficiencies Detected Across All Classes: ").append(totalOverallInefficiencies).append("</strong></p>\n");

        reportContent.append("<hr>\n");

        reportContent.append("<h2>Detailed Reports by Class:</h2>\n");
        for (InefficiencyAnalyzerRunner runner : runners) {
            if (!runner.isEfficient()) {
                String className = runner.getJavaFile().getName();
                String reportTitle = "Inefficiency Report for " + className;
                String linkId = className.replace(".java", "").toLowerCase() + "-report";
                reportContent.append("<h3 id='").append(linkId).append("'>").append(reportTitle).append("</h3>\n");
                String[][] recommendations = runner.getRecommendedData();
                if (recommendations != null && recommendations.length > 0) {
                    reportContent.append("<table>\n");
                    reportContent.append("<tr><th>Analyzer</th><th>Inefficient Code</th><th>Efficient Code</th></tr>\n");
                    for (String[] recommendation : recommendations) {
                        if (recommendation.length == 3) {
                            reportContent.append("<tr><td>").append(escapeHtml(recommendation[0])).append("</td><td><pre><code>").append(escapeHtml(recommendation[1])).append("</code></pre></td><td><pre><code>").append(escapeHtml(recommendation[2])).append("</code></pre></td></tr>\n");
                        } else if (recommendation.length == 2) {
                            reportContent.append("<tr><td>").append(escapeHtml(recommendation[0])).append("</td><td><pre><code>").append(escapeHtml(recommendation[1])).append("</code></pre></td><td>N/A</td></tr>\n");
                        }
                    }
                    reportContent.append("</table>\n");
                } else {
                    reportContent.append("<p>No specific recommendations found for this class.</p>\n");
                }
                reportContent.append("<hr>\n");
            }
        }

        HtmlReport.generateHtmlReport("Combined Inefficiency Analysis Report", reportContent.toString(), reportPath);
        Debug.info("Combined analysis report generated at: " + reportPath);
    }

    private static String escapeHtml(String text) {
        if (text == null) {
            return "";
        }
        return text.replace("<", "&lt;")
                .replace(">", "&gt;");
    }
}