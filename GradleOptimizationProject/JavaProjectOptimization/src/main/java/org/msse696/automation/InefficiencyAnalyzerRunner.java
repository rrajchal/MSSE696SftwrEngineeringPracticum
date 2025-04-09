package org.msse696.automation;

import lombok.Getter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The {@code InefficiencyAnalyzerRunner} class orchestrates the analysis of Java source code
 * for various types of inefficiencies. It utilizes a collection of individual {@link Analyzer}
 * implementations to identify potential performance bottlenecks and provides a combined HTML report
 * summarizing the findings and offering recommendations.
 *
 * <p>This class implements the {@link Analyzer} interface, allowing it to be integrated into
 * a larger analysis pipeline if needed. It manages the execution of multiple specific analyzers
 * and consolidates their recommended inefficient and efficient code examples into a single report.
 *
 * <p>The generated HTML report lists the analyzers that detected inefficiencies and provides
 * clickable links to the corresponding recommendation tables for each analyzer. It also displays
 * the total number of inefficiencies detected across all analyzers.
 */
public class InefficiencyAnalyzerRunner implements Analyzer {

    private final List<Analyzer> analyzers;
    private final String outputReportPath = "target/results/reports/combined_inefficiency_report.html";
    private final Map<String, String[][]> analyzerRecommendations = new HashMap<>();
    private final List<String> analyzedAnalyzers = new ArrayList<>();
    private boolean isEfficientOverall = true;
    @Getter
    private int totalInefficiencyCount = 0;

    public InefficiencyAnalyzerRunner() {
        analyzers = new ArrayList<>();
        analyzers.add(new ArithmeticOperationAnalyzer());
        analyzers.add(new ArrayCopyInefficiencyAnalyzer());
        analyzers.add(new CastingAnalyzer());
        analyzers.add(new ExpressionEliminationAnalyzer());
        analyzers.add(new InstanceofAnalyzer());
        analyzers.add(new LoopInefficiencyAnalyzer());
        analyzers.add(new LoopObjectCreationAnalyzer());
        analyzers.add(new PaddingAnalyzer());
        analyzers.add(new StringConcatenationAnalyzer());
        analyzers.add(new TryCatchAnalyzer());
    }

    @Override
    public boolean analyze(File javaFile, boolean createReport) {
        if (!javaFile.exists() || !javaFile.isFile() || !javaFile.getName().endsWith(".java")) {
            System.err.println("Invalid Java file path provided.");
            isEfficientOverall = false;
            return false;
        }

        System.out.println("Starting inefficiency analysis for: " + javaFile.getName());

        for (Analyzer analyzer : analyzers) {
            String analyzerName = analyzer.getClass().getSimpleName();
            System.out.println("\nRunning analyzer: " + analyzerName);
            boolean inefficienciesFound = analyzer.analyze(javaFile, false); // Don't create individual reports
            if (inefficienciesFound) {
                isEfficientOverall = false;
                analyzedAnalyzers.add(analyzerName);
                String[][] recommendations = analyzer.getRecommendedData();
                if (recommendations != null && recommendations.length > 2) {
                    analyzerRecommendations.put(analyzerName, new String[][]{
                            {"Inefficient Code", "Efficient Code"},
                            {recommendations[1][1], recommendations[2][1]}
                    });
                    totalInefficiencyCount++; // Increment count for each recommendation set
                } else if (recommendations != null && recommendations.length > 1) {
                    analyzerRecommendations.put(analyzerName, new String[][]{
                            {"Inefficient Code", "Efficient Code"},
                            {recommendations[1][1], "N/A"}
                    });
                    totalInefficiencyCount++; // Increment count for each recommendation set
                }
            }
        }

        if (createReport) {
            generateCombinedReport(javaFile.getName());
            System.out.println("\nAnalysis complete. Combined report generated: " + outputReportPath);
        } else {
            System.out.println("\nAnalysis complete.");
        }

        return !isEfficientOverall;
    }

    @Override
    public void generateReport(String title, String actualHeader, String[][] actualData, String recommendedHeader, String[][] recommendedData, String outputPath) {
        // This method is overridden by generateCombinedReport for the specific output format
    }

    private void generateCombinedReport(String analyzedFileName) {
        File outputFile = new File(outputReportPath);
        File parentDir = outputFile.getParentFile();
        if (!parentDir.exists() && !parentDir.mkdirs()) {
            System.err.println("Failed to create output directory: " + parentDir.getAbsolutePath());
            return;
        }

        StringBuilder htmlContent = new StringBuilder();
        htmlContent.append("<!DOCTYPE html>");
        htmlContent.append("<html lang='en'>");
        htmlContent.append("<head>");
        htmlContent.append("<meta charset='UTF-8'>");
        htmlContent.append("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        htmlContent.append("<title>Combined Inefficiency Analysis Report</title>");
        htmlContent.append("<style>");
        htmlContent.append("body { font-family: Arial, sans-serif; margin: 20px; }");
        htmlContent.append(".container { max-width: 800px; margin: auto; }");
        htmlContent.append("h1 { text-align: center; color: navy; }");
        htmlContent.append("h2 { color: darkred; margin-top: 30px; }");
        htmlContent.append("h3 { color: darkgreen; margin-top: 20px; }");
        htmlContent.append("p { font-weight: bold; margin-top: 15px; }");
        htmlContent.append("pre { background-color: #f4f4f4; padding: 10px; border: 1px solid #ccc; overflow-x: auto; white-space: pre-wrap; word-wrap: break-word; }");
        htmlContent.append("code { font-family: monospace; }");
        htmlContent.append("ul { list-style-type: none; padding: 0; }");
        htmlContent.append("li a { color: blue; text-decoration: none; }");
        htmlContent.append("li a:hover { text-decoration: underline; }");
        htmlContent.append("table { width: 100%; border-collapse: collapse; margin-top: 10px; }");
        htmlContent.append("th, td { border: 1px solid #ccc; padding: 8px; text-align: left; }");
        htmlContent.append("th { background-color: #f0f0f0; }");
        htmlContent.append("</style>");
        htmlContent.append("</head>");
        htmlContent.append("<body>");
        htmlContent.append("<div class='container'>");
        htmlContent.append("<h1>Combined Inefficiency Analysis Report</h1>");

        if (!analyzedAnalyzers.isEmpty()) {
            htmlContent.append("<h2>Analyzers that Detected Inefficiencies:</h2>");
            htmlContent.append("<ul>");
            for (String analyzerName : analyzedAnalyzers) {
                String linkId = analyzerName.toLowerCase().replace("analyzer", "").trim() + "-recommendations";
                htmlContent.append("<li><a href='#").append(linkId).append("'>").append(analyzerName).append("</a></li>");
            }
            htmlContent.append("</ul>");
            htmlContent.append("<p><strong>Total Inefficiencies Detected: ").append(totalInefficiencyCount).append("</strong></p>");

            htmlContent.append("<h2>Recommendations by Analyzer:</h2>");
            for (Map.Entry<String, String[][]> entry : analyzerRecommendations.entrySet()) {
                String analyzerName = entry.getKey();
                String[][] recommendations = entry.getValue();
                String tableId = analyzerName.toLowerCase().replace("analyzer", "").trim() + "-recommendations";
                htmlContent.append("<h3 id='").append(tableId).append("'>").append(analyzerName).append("</h3>");
                htmlContent.append("<table>");
                if (recommendations != null && recommendations.length > 0) {
                    // Header row
                    htmlContent.append("<tr>");
                    for (String header : recommendations[0]) {
                        htmlContent.append("<th>").append(header).append("</th>");
                    }
                    htmlContent.append("</tr>");
                    // Data rows
                    for (int i = 1; i < recommendations.length; i++) {
                        htmlContent.append("<tr>");
                        for (String cell : recommendations[i]) {
                            htmlContent.append("<td><pre><code>").append(cell).append("</code></pre></td>");
                        }
                        htmlContent.append("</tr>");
                    }
                } else {
                    htmlContent.append("<tr><td colspan='2'>No specific recommendations found by this analyzer.</td></tr>");
                }
                htmlContent.append("</table>");
            }

        } else {
            htmlContent.append("<p>No inefficiencies detected by any of the analyzers in ").append(analyzedFileName).append("</p>");
        }

        htmlContent.append("</div>");
        htmlContent.append("</body>");
        htmlContent.append("</html>");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            writer.write(htmlContent.toString());
            System.out.println("Combined HTML report generated successfully: " + outputReportPath);
        } catch (IOException e) {
            System.err.println("Error while writing the combined HTML file: " + outputReportPath);
        }
    }

    @Override
    public String getReport() {
        return outputReportPath;
    }

    @Override
    public boolean isEfficient() {
        return isEfficientOverall;
    }

    @Override
    public String[][] getRecommendedData() {
        // Not directly used for the combined report's format
        return new String[][]{};
    }
}