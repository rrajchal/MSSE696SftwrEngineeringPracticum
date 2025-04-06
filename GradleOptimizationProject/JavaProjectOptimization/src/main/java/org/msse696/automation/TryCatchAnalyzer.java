package org.msse696.automation;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.TryStmt;
import org.msse696.optimization.helper.report.HtmlReport;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * The TryCatchAnalyzer class detects the placement of try-catch blocks
 * within or outside loops in Java files and evaluates efficiency.
 */
public class TryCatchAnalyzer implements Analyzer {

    private static final String OUTPUT_REPORT = "target/results/reports/try_catch_analysis_report.html";

    /**
     * Analyzes the placement of try-catch blocks within the given Java file.
     *
     * @param javaFile The Java file to analyze.
     * @return True if inefficiencies are detected, false otherwise.
     */
    @Override
    public boolean analyze(File javaFile) {
        System.out.println("Analyzing file: " + javaFile.getName());
        List<String[]> inefficiencies = new ArrayList<>(); // Tracks inefficiencies for report generation
        boolean inefficiencyDetected = false;

        try (FileInputStream fileInputStream = new FileInputStream(javaFile)) {
            // Parse the Java file into a CompilationUnit
            CompilationUnit compilationUnit = StaticJavaParser.parse(fileInputStream);
            System.out.println("Parsed CompilationUnit:\n" + compilationUnit);

            // Analyze methods within the file
            for (MethodDeclaration method : compilationUnit.findAll(MethodDeclaration.class)) {
                System.out.println("Analyzing method: " + method.getName());
                boolean methodHasInefficiency = detectTryCatchInLoop(method);
                if (methodHasInefficiency) {
                    inefficiencyDetected = true;
                    inefficiencies.add(new String[]{method.getNameAsString(), "Inefficient try-catch placement detected"});
                }
            }

        } catch (Exception e) {
            System.err.println("Error analyzing file: " + javaFile.getPath());
        }

        // Only generate a report if inefficiencies are detected
        if (inefficiencyDetected) {
            System.out.println("\nInefficiencies detected. Generating report...");
            generateReport(
                    "Try-Catch Placement Analysis Report",
                    "Methods with Inefficient Try-Catch Placement",
                    prepareReportData(inefficiencies),
                    "Recommendations for Optimization",
                    getRecommendedData(),
                    OUTPUT_REPORT
            );
        } else {
            System.out.println("\nNo inefficiencies detected. Report will not be generated.");
        }

        return inefficiencyDetected;
    }

    /**
     * Detects try-catch placement inefficiencies within a method's loops.
     *
     * @param method The method to analyze.
     * @return True if inefficiencies are detected, false otherwise.
     */
    private boolean detectTryCatchInLoop(MethodDeclaration method) {
        List<ForStmt> loops = method.findAll(ForStmt.class); // Get all loops within the method

        for (ForStmt loop : loops) {
            // Check if the loop contains a try-catch block directly
            if (!loop.findAll(TryStmt.class).isEmpty()) {
                System.out.println("Inefficient try-catch placement detected inside loop.");
                return true; // Inefficient placement detected
            }

            // Check if the loop is wrapped by a try-catch block
            BlockStmt parentBlock = (BlockStmt) loop.getParentNode()
                    .filter(node -> node instanceof BlockStmt).orElse(null);

            if (parentBlock != null && !parentBlock.findAll(TryStmt.class).isEmpty()) {
                System.out.println("Try-catch block found outside loop (efficient placement).");
            }
        }

        return false; // No inefficiencies detected
    }

    /**
     * Prepares report data from inefficient methods detected.
     *
     * @param inefficiencies The list of methods with inefficiencies.
     * @return A 2D array for report generation.
     */
    private String[][] prepareReportData(List<String[]> inefficiencies) {
        String[][] data = new String[inefficiencies.size() + 1][2];
        data[0] = new String[]{"Method Name", "Issue"};
        for (int i = 0; i < inefficiencies.size(); i++) {
            data[i + 1] = inefficiencies.get(i);
        }
        return data;
    }

    /**
     * Generates an HTML report summarizing the analysis and recommendations.
     *
     * @param title            The title of the report.
     * @param actualHeader     Header for detected inefficiencies.
     * @param actualData       Data showing inefficiencies.
     * @param recommendedHeader Header for optimization suggestions.
     * @param recommendedData  Data providing optimization recommendations.
     * @param outputPath       Path to save the HTML report.
     */
    @Override
    public void generateReport(String title, String actualHeader, String[][] actualData,
                               String recommendedHeader, String[][] recommendedData, String outputPath) {
        HtmlReport.generateHtmlReport(title, actualHeader, actualData, recommendedHeader, recommendedData, outputPath);
    }

    /**
     * Returns the path to the generated HTML report.
     *
     * @return The report file path.
     */
    @Override
    public String getReport() {
        return OUTPUT_REPORT;
    }

    /**
     * Returns the recommended optimization examples for inefficient and efficient code.
     *
     * @return A 2D array containing examples of inefficient and efficient code placement.
     */
    private String[][] getRecommendedData() {
        return new String[][]{
                {"Inefficient Code", """
            <pre><code>
            public void execute(int iterations) {
                for (int i = 0; i < iterations; i++) {
                    try {
                        performOperation();
                    } catch (Exception e) {
                        System.err.println("An exception occurred: " + e.getMessage());
                    }
                }
            }
            </code></pre>
            """},
                {"Efficient Code", """
            <pre><code>
            public void execute(int iterations) {
                try {
                    for (int i = 0; i < iterations; i++) {
                        performOperation();
                    }
                } catch (Exception e) {
                    System.err.println("An exception occurred: " + e.getMessage());
                }
            }
            </code></pre>
            """}
        };
    }
}
