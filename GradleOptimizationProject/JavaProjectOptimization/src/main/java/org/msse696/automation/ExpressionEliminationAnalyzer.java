package org.msse696.automation;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.Statement;
import org.msse696.optimization.helper.report.HtmlReport;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * The ExpressionEliminationAnalyzer class detects common subexpressions
 * calculated multiple times within loops and suggests caching them.
 */
public class ExpressionEliminationAnalyzer implements Analyzer {

    private static final String OUTPUT_REPORT = "target/results/reports/expression_elimination_report.html";

    /**
     * Analyzes a Java file to detect redundant calculations of common subexpressions within loops.
     *
     * @param javaFile The Java file to analyze.
     * @return True if inefficiencies are detected, false otherwise.
     */
    @Override
    public boolean analyze(File javaFile) {
        AtomicBoolean optimizationNeeded = new AtomicBoolean(false); // AtomicBoolean for thread safety
        List<String[]> inefficientMethods = new ArrayList<>();

        System.out.println("Analyzing file: " + javaFile.getName());

        try (FileInputStream fileInputStream = new FileInputStream(javaFile)) {
            CompilationUnit compilationUnit = StaticJavaParser.parse(fileInputStream); // Parse the file
            System.out.println("Parsed CompilationUnit:\n" + compilationUnit);

            // Analyze each method in the file
            compilationUnit.findAll(MethodDeclaration.class).forEach(method -> {
                System.out.println("Analyzing method: " + method.getName());

                if (detectCommonSubexpressions(method)) {
                    optimizationNeeded.set(true); // Optimization needed for this method
                    inefficientMethods.add(new String[]{method.getNameAsString(), "Redundant calculations detected"});
                    System.out.println("Issue detected in method: " + method.getName());
                } else {
                    System.out.println("No issues detected in method: " + method.getName());
                }
            });
        } catch (Exception e) {
            System.err.println("Error analyzing file: " + javaFile.getPath());
        }

        // Generate report if inefficiencies are detected
        if (optimizationNeeded.get()) {
            System.out.println("\nOptimization is required. Creating report...");
            generateReport(
                    "Expression Elimination Analysis Report",
                    "Methods with Redundant Calculations",
                    prepareReportData(inefficientMethods),
                    "Recommendations for Optimization",
                    generateRecommendedData(),
                    OUTPUT_REPORT
            );
        } else {
            System.out.println("\nNo optimization required. Report will not be generated.");
        }

        return optimizationNeeded.get();
    }

    /**
     * Detects redundant calculations of common subexpressions within loops.
     *
     * @param method The method to analyze.
     * @return True if inefficiencies are detected, false otherwise.
     */
    private boolean detectCommonSubexpressions(MethodDeclaration method) {
        List<ForStmt> loops = method.findAll(ForStmt.class); // Retrieve all for-loops in the method

        for (ForStmt loop : loops) {
            System.out.println("Analyzing loop body: " + loop.getBody());

            Map<String, Integer> expressionCount = new HashMap<>(); // Tracks occurrences of subexpressions

            // Analyze statements within the loop body
            if (loop.getBody().isBlockStmt()) {
                for (Statement statement : loop.getBody().asBlockStmt().getStatements()) {
                    analyzeStatement(statement, expressionCount);
                }
            } else {
                analyzeStatement(loop.getBody(), expressionCount);
            }

            // Check for redundant expressions
            for (Map.Entry<String, Integer> entry : expressionCount.entrySet()) {
                if (entry.getValue() > 1) { // Expression occurs more than once
                    System.out.println("Redundant calculation detected: " + entry.getKey());
                    return true;
                }
            }
        }
        return false; // No redundant calculations detected
    }

    /**
     * Analyzes a statement to track the occurrences of subexpressions.
     *
     * @param statement       The statement to analyze.
     * @param expressionCount A map to track the frequency of each expression.
     */
    private void analyzeStatement(Statement statement, Map<String, Integer> expressionCount) {
        if (statement.isExpressionStmt()) {
            Expression expression = statement.asExpressionStmt().getExpression();
            System.out.println("Expression type: " + expression.getClass().getSimpleName());

            // Handle VariableDeclarationExpr (e.g., int x = 10;)
            if (expression instanceof VariableDeclarationExpr variableDeclarationExpr) {
                variableDeclarationExpr.getVariables().forEach(variable -> {
                    if (variable.getInitializer().isPresent()) {
                        trackSubExpressions(variable.getInitializer().get(), expressionCount); // Analyze initializer
                    }
                });
            }

            // Handle AssignExpr (e.g., x = y + z;)
            else if (expression instanceof AssignExpr assignExpr) {
                trackSubExpressions(assignExpr.getValue(), expressionCount);
            }
        }
    }


    /**
     * Tracks subexpressions recursively within a given expression.
     *
     * @param expression      The expression to analyze.
     * @param expressionCount A map to track the frequency of each subexpression.
     */
    private void trackSubExpressions(Expression expression, Map<String, Integer> expressionCount) {
        if (expression instanceof BinaryExpr binaryExpr) {
            // Record the binary expression as a string
            String exprString = binaryExpr.toString();
            expressionCount.put(exprString, expressionCount.getOrDefault(exprString, 0) + 1);

            // Recursively analyze left and right operands
            trackSubExpressions(binaryExpr.getLeft(), expressionCount);
            trackSubExpressions(binaryExpr.getRight(), expressionCount);

        } else if (expression instanceof MethodCallExpr methodCallExpr) {
            // Record the method call expression
            String methodCallString = methodCallExpr.toString();
            expressionCount.put(methodCallString, expressionCount.getOrDefault(methodCallString, 0) + 1);

            // Analyze arguments passed to the method call
            methodCallExpr.getArguments().forEach(arg -> trackSubExpressions(arg, expressionCount));
        }
    }

    /**
     * Prepares report data from inefficient methods detected.
     *
     * @param inefficientMethods The list of inefficient methods detected.
     * @return The 2D array for report generation.
     */
    private String[][] prepareReportData(List<String[]> inefficientMethods) {
        String[][] data = new String[inefficientMethods.size() + 1][2];
        data[0] = new String[]{"Method Name", "Issue"};
        for (int i = 0; i < inefficientMethods.size(); i++) {
            data[i + 1] = inefficientMethods.get(i);
        }
        return data;
    }

    /**
     * Generates the recommended improvements for detected inefficiencies.
     *
     * @return A 2D array containing recommendations and examples of optimizations.
     */
    public String[][] generateRecommendedData() {
        return new String[][]{
                {"Recommendation", "Cache intermediate results of common subexpressions to avoid redundant calculations."},
                {"Example (Inefficient)", """
            <pre><code>
            int z1 = x * Math.abs(y) + x;
            int z2 = x * Math.abs(y) + y;
            </code></pre>
            """},
                {"Example (Optimized)", """
            <pre><code>
            int t1 = x * Math.abs(y);
            int z1 = t1 + x;
            int z2 = t1 + y;
            </code></pre>
            """}
        };
    }

    @Override
    public void generateReport(String title, String actualHeader, String[][] actualData, String recommendedHeader, String[][] recommendedData, String outputPath) {
        HtmlReport.generateHtmlReport(title, actualHeader, actualData, recommendedHeader, recommendedData, outputPath);
    }

    @Override
    public String getReport() {
        return OUTPUT_REPORT;
    }
}
