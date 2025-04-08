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
 * The LoopInefficiencyAnalyzer class detects inefficient practices
 * in loops, such as repeated calculations or method calls.
 */
public class LoopInefficiencyAnalyzer implements Analyzer {

    private static final String OUTPUT_REPORT = "target/results/reports/loop_inefficiency_report.html";
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
                boolean methodHasInefficiency = detectLoopInefficiency(method);
                if (methodHasInefficiency) {
                    inefficiencyDetected = true;
                    inefficiencies.add(new String[]{method.getNameAsString(), "Repeated calculations or method calls in loop detected"});
                }
            }

        } catch (Exception e) {
            System.err.println("Error analyzing file: " + javaFile.getPath());
        }

        // Generate a report if inefficiencies are detected
        if (inefficiencyDetected && createReport) {
            System.out.println("\nInefficiencies detected. Generating report...");
            generateReport(
                "Loop Inefficiency Analysis Report",
                "Methods with Inefficient Loops",
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
     * Detects inefficiencies in loops within the method, such as repeated calculations in loop conditions.
     *
     * @param method The method to analyze.
     * @return True if inefficiencies are detected, false otherwise.
     */
    private boolean detectLoopInefficiency(MethodDeclaration method) {
        List<ForStmt> forStatements = method.findAll(ForStmt.class);

        for (ForStmt forStmt : forStatements) {
            // Check for method calls in the loop termination condition
            if (forStmt.getCompare().isPresent()) {
                List<MethodCallExpr> conditionMethodCalls = forStmt.getCompare().get().findAll(MethodCallExpr.class);

                if (!conditionMethodCalls.isEmpty()) {
                    System.out.println("Method call detected in loop termination condition: " + conditionMethodCalls);
                    return true; // Inefficiency detected in termination condition
                }
            }

            // Check the loop body for redundant method calls
            List<MethodCallExpr> bodyMethodCalls = forStmt.getBody().findAll(MethodCallExpr.class);
            if (containsRedundantMethodCalls(bodyMethodCalls)) {
                System.out.println("Repeated method calls detected in loop body: " + bodyMethodCalls);
                return true; // Inefficiency detected in the loop body
            }
        }

        return false; // No inefficiencies detected
    }

    /**
     * Checks if there are redundant method calls within the given list of MethodCallExpr.
     *
     * @param methodCalls The list of method calls to analyze.
     * @return True if redundant method calls are detected, false otherwise.
     */
    private boolean containsRedundantMethodCalls(List<MethodCallExpr> methodCalls) {
        List<String> calledMethods = new ArrayList<>();
        for (MethodCallExpr methodCall : methodCalls) {
            String methodName = methodCall.getNameAsString();
            if (calledMethods.contains(methodName)) {
                return true; // Redundant method call detected
            }
            calledMethods.add(methodName);
        }
        return false; // No redundant calls detected
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
            for (int i = 0; i < size.length(); i++) { // Method called during iteration
                performOperation(size.charAt(i));
            }
            </code></pre>
            """},
            {"Efficient Code", """
            <pre><code>
            int length = size.length(); // Precompute loop limit
            for (int i = 0; i < length; i++) {
                performOperation(size.charAt(i));
            }
            </code></pre>
            """}
        };
    }
}
