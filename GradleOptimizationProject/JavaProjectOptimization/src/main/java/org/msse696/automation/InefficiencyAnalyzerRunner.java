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
 * recommended data.
 */
public class InefficiencyAnalyzerRunner implements Analyzer {
    @Getter
    private File javaFile;
    private final List<Analyzer> analyzers;
    final String outputReportPath = "target/results/reports/combined_inefficiency_report.html";
    final Map<String, List<String[]>> analyzerRecommendations = new HashMap<>();
    final List<String> analyzedAnalyzers = new ArrayList<>();
    private boolean isEfficientOverall = true;
    @Getter
    private int totalInefficiencyCount = 0;
    @Getter
    List<String[]> allRecommendations = new ArrayList<>();

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
            Debug.error("Invalid Java file path provided.");
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
                    //recommendationsList.add(new String[]{"Analyzer", "Inefficient Code", "Efficient Code"});
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
        return !isEfficientOverall;
    }

    @Override
    public void generateReport(String title, String actualHeader, String[][] actualData, String recommendedHeader, String[][] recommendedData, String outputPath) {
        HtmlReport.generateHtmlTableReport(title, actualHeader, actualData, recommendedHeader, recommendedData, outputPath);
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
        // Return all collected recommendations (without the header)
        if (allRecommendations.size() > 1) {
            List<String[]> dataOnly = new ArrayList<>();
            for (int i = 1; i < allRecommendations.size(); i++) {
                dataOnly.add(allRecommendations.get(i));
            }
            return dataOnly.toArray(new String[0][]);
        }
        return new String[0][0];
    }
}