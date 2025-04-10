package org.msse696.automation;

import lombok.Getter;
import org.msse696.optimization.helper.debug.Debug;
import org.msse696.optimization.helper.report.HtmlReport;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The {@code InefficiencyAnalyzerRunner} class orchestrates the analysis of Java source code
 * for various types of inefficiencies. It utilizes a collection of individual {@link Analyzer}
 * implementations to identify potential performance bottlenecks and provides access to the
 * recommended data. It leverages the {@link HtmlReport} class for generating reports.
 *
 * <p>This class implements the {@link Analyzer} interface, allowing it to be integrated into
 * a larger analysis pipeline. It manages the execution of multiple specific analyzers
 * and collects their recommended inefficient and efficient code examples.
 */
public class InefficiencyAnalyzerRunner implements Analyzer {
    private File javaFile;
    private final List<Analyzer> analyzers;
    private final String outputReportPath = "target/results/reports/combined_inefficiency_report.html";
    private final Map<String, List<String[]>> analyzerRecommendations = new HashMap<>();
    private final List<String> analyzedAnalyzers = new ArrayList<>();
    private boolean isEfficientOverall = true;
    @Getter
    private int totalInefficiencyCount = 0;
    @Getter
    private List<String[]> allRecommendations = new ArrayList<>();

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
        this.javaFile = javaFile;
        Debug.info("Starting inefficiency analysis for: " + javaFile.getName());

        for (Analyzer analyzer : analyzers) {
            String analyzerName = analyzer.getClass().getSimpleName();
            Debug.info("\nRunning analyzer: " + analyzerName);
            boolean inefficienciesFound = analyzer.analyze(javaFile, false); // Don't create individual reports
            if (inefficienciesFound) {
                isEfficientOverall = false;
                analyzedAnalyzers.add(analyzerName);
                String[][] recommendations = analyzer.getRecommendedData();
                if (recommendations != null && recommendations.length > 1) {
                    List<String[]> recommendationsList = new ArrayList<>();
                    recommendationsList.add(new String[]{"Analyzer", "Inefficient Code", "Efficient Code"});
                    if (recommendations.length > 2) {
                        recommendationsList.add(new String[]{analyzerName, recommendations[1][1], recommendations[2][1]});
                        totalInefficiencyCount++;
                    } else {
                        recommendationsList.add(new String[]{analyzerName, recommendations[1][1], "N/A"});
                        totalInefficiencyCount++;
                    }
                    analyzerRecommendations.put(analyzerName, recommendationsList);
                    allRecommendations.addAll(recommendationsList);
                }
            }
        }

        if (createReport) {
            generateReport("Inefficiency Analysis Report for " + javaFile.getName(),
                    "Detected Inefficiencies",
                    prepareActualData(),
                    "Recommendations",
                    prepareRecommendedData(),
                    outputReportPath);
            Debug.info("\nAnalysis complete. Report generated: " + outputReportPath);
        } else {
            Debug.info("\nAnalysis complete.");
        }

        return !isEfficientOverall;
    }

    private String[][] prepareActualData() {
        List<String[]> actualDataList = new ArrayList<>();
        actualDataList.add(new String[]{"Analyzer", "Status"});
        for (String analyzer : analyzedAnalyzers) {
            actualDataList.add(new String[]{analyzer, "Detected Inefficiencies"});
        }
        if (analyzedAnalyzers.isEmpty()) {
            actualDataList.add(new String[]{"No Analyzer", "No Inefficiencies Detected"});
        }
        return actualDataList.toArray(new String[0][]);
    }

    private String[][] prepareRecommendedData() {
        List<String[]> recommendedDataList = new ArrayList<>();
        if (!allRecommendations.isEmpty()) {
            recommendedDataList.addAll(allRecommendations);
        } else {
            recommendedDataList.add(new String[]{"No Recommendations", "N/A", "N/A"});
        }
        return recommendedDataList.toArray(new String[0][]);
    }

    @Override
    public void generateReport(String title, String actualHeader, String[][] actualData, String recommendedHeader, String[][] recommendedData, String outputPath) {
        HtmlReport.generateHtmlReport(title, actualHeader, actualData, recommendedHeader, recommendedData, outputPath);
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
        // Return all collected recommendations
        return allRecommendations.toArray(new String[0][]);
    }
}