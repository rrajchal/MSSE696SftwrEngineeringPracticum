package org.msse696.automation;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.stmt.ForStmt;
import org.msse696.optimization.helper.report.HtmlReport;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * The ArrayCopyInefficiencyAnalyzer class detects inefficiencies in array copying practices,
 * such as using manual loops instead of System.arraycopy().
 */
public class ArrayCopyInefficiencyAnalyzer implements Analyzer {

    private static final String OUTPUT_REPORT = "target/results/reports/array_copy_inefficiency_report.html";
    private boolean isEfficient;

    @Override
    public boolean analyze(File javaFile, boolean createReport) {
        System.out.println("Analyzing file: " + javaFile.getName());
        List<String[]> inefficiencies = new ArrayList<>();
        boolean inefficiencyDetected = false;

        try (FileInputStream fileInputStream = new FileInputStream(javaFile)) {
            // Parse the Java file into a CompilationUnit
            CompilationUnit compilationUnit = StaticJavaParser.parse(fileInputStream);
            System.out.println("Parsed CompilationUnit:\n" + compilationUnit);

            // Analyze methods within the file
            for (MethodDeclaration method : compilationUnit.findAll(MethodDeclaration.class)) {
                System.out.println("Analyzing method: " + method.getName());
                boolean methodHasInefficiency = detectInefficientArrayCopying(method);
                if (methodHasInefficiency) {
                    inefficiencyDetected = true;
                    inefficiencies.add(new String[]{method.getNameAsString(), "Manual array copying using loops detected"});
                }
            }

        } catch (Exception e) {
            System.err.println("Error analyzing file: " + javaFile.getPath());
        }

        // Generate a report if inefficiencies are detected
        if (inefficiencyDetected && createReport) {
            System.out.println("\nInefficiencies detected. Generating report...");
            generateReport(
                "Array Copy Inefficiency Analysis Report",
                "Methods with Inefficient Array Copying",
                prepareReportData(inefficiencies),
                "Recommendations for Optimization",
                getRecommendedData(),
                OUTPUT_REPORT
            );
        } else {
            System.out.println("\nNo inefficiencies detected. Report will not be generated.");
        }
        isEfficient = !inefficiencyDetected;
        return inefficiencyDetected;
    }

    /**
     * Detects inefficient array copying practices in the given method.
     *
     * @param method The method to analyze.
     * @return True if inefficiencies are detected, false otherwise.
     */
    private boolean detectInefficientArrayCopying(MethodDeclaration method) {
        // Check for manual loops that perform array copying
        List<ForStmt> forStatements = method.findAll(ForStmt.class);

        for (ForStmt forStmt : forStatements) {
            List<MethodCallExpr> methodCalls = forStmt.findAll(MethodCallExpr.class);

            // Check if System.arraycopy is used
            boolean systemArrayCopyUsed = methodCalls.stream()
                .anyMatch(methodCall -> methodCall.getNameAsString().equals("arraycopy"));

            if (!systemArrayCopyUsed) {
                System.out.println("Manual loop detected for array copying in method: " + method.getName());
                return true; // Inefficient loop detected
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

    @Override
    public String getReport() {
        return OUTPUT_REPORT;
    }

    @Override
    public boolean isEfficient() {
        return isEfficient;
    }

    @Override
    public String[][] getRecommendedData() {
        return new String[][]{
            {"Example", "Code"},
            {"Inefficient Code", """
            <pre><code>
            public void copyArray(int size) {
                int[] source = new int[size];
                int[] destination = new int[size];
                // Fill source array with dummy data
                Arrays.fill(source, 1);
                // Manual copy using a loop
                for (int i = 0; i < size; i++) {
                    destination[i] = source[i];
                }
            }
            </code></pre>
            """},
            {"Efficient Code", """
            <pre><code>
            public void copyArray(int size) {
                int[] source = new int[size];
                int[] destination = new int[size];
                // Fill source array with dummy data
                Arrays.fill(source, 1);
                // Use System.arraycopy
                System.arraycopy(source, 0, destination, 0, size);
            }
            </code></pre>
            """}
        };
    }
}
