package org.msse696.automation;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.Statement;
import org.msse696.optimization.helper.report.HtmlReport;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * The ArithmeticOperationAnalyzer detects inefficient arithmetic operations that can be replaced with bitwise operations.
 */
public class ArithmeticOperationAnalyzer implements Analyzer {

    private static final String OUTPUT_REPORT = "target/results/reports/arithmetic_operation_report.html";

    /**
     * Analyzes a Java file to detect inefficient arithmetic operations inside loops.
     *
     * @param javaFile The Java file to analyze.
     * @return True if optimization is needed, false otherwise.
     */
    @Override
    public boolean analyze(File javaFile) {
        AtomicBoolean optimizationNeeded = new AtomicBoolean(false);
        List<String[]> inefficientMethods = new ArrayList<>();

        System.out.println("Analyzing file: " + javaFile.getName());

        try (FileInputStream fileInputStream = new FileInputStream(javaFile)) {
            // Parse the file
            CompilationUnit compilationUnit = StaticJavaParser.parse(fileInputStream);
            System.out.println("Parsed CompilationUnit:\n" + compilationUnit.toString());

            // Analyze each method in the Java file
            compilationUnit.findAll(MethodDeclaration.class).forEach(method -> {
                System.out.println("Analyzing method: " + method.getName());

                if (detectInefficientOperations(method)) {
                    optimizationNeeded.set(true);
                    inefficientMethods.add(new String[]{method.getNameAsString(), "Inefficient arithmetic operations detected"});
                    System.out.println("Issue detected in method: " + method.getName());
                } else {
                    System.out.println("No issues detected in method: " + method.getName());
                }
            });
        } catch (Exception e) {
            System.err.println("Error analyzing file: " + javaFile.getPath());
            e.printStackTrace();
        }

        // Generate report if inefficiencies were detected
        if (optimizationNeeded.get()) {
            System.out.println("\nOptimization is required. Creating report...");
            generateReport(
                "Arithmetic Operation Analysis Report",
                "Methods with Inefficient Arithmetic Operations",
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
     * Detects inefficient arithmetic operations that could be replaced with bitwise operations.
     *
     * @param method The method to analyze.
     * @return True if inefficient operations are detected, false otherwise.
     */
    private boolean detectInefficientOperations(MethodDeclaration method) {
        List<ForStmt> loops = method.findAll(ForStmt.class);

        for (ForStmt loop : loops) {
            System.out.println("Analyzing loop body: " + loop.getBody());

            if (loop.getBody().isBlockStmt()) {
                for (Statement statement : loop.getBody().asBlockStmt().getStatements()) {
                    System.out.println("Statement found: " + statement);

                    if (checkForInefficiency(statement)) {
                        return true;
                    }
                }
            } else if (checkForInefficiency(loop.getBody())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks a given statement for inefficient arithmetic operations.
     *
     * @param statement The statement to check.
     * @return True if inefficient operations are detected, false otherwise.
     */
    private boolean checkForInefficiency(Statement statement) {
        // Detect inefficient multiplication and division
        if (statement.isExpressionStmt() && statement.asExpressionStmt().getExpression() instanceof AssignExpr) {
            AssignExpr assignExpr = statement.asExpressionStmt().getExpression().asAssignExpr();
            System.out.println("Analyzing Assignment Expression: " + assignExpr);

            if (assignExpr.getOperator() == AssignExpr.Operator.ASSIGN) {
                if (assignExpr.getValue() instanceof BinaryExpr) {
                    BinaryExpr binaryExpr = assignExpr.getValue().asBinaryExpr();

                    // Detect multiplication by 2 (x * 2) or division by 2 (x / 2)
                    if (binaryExpr.getOperator() == BinaryExpr.Operator.MULTIPLY || binaryExpr.getOperator() == BinaryExpr.Operator.DIVIDE) {
                        if (binaryExpr.getRight().isLiteralExpr()) {
                            // Check if the right operand is the literal value 2
                            String literalValue = binaryExpr.getRight().asLiteralExpr().toString();
                            if ("2".equals(literalValue)) {
                                System.out.println("Inefficient arithmetic operation detected: " + binaryExpr);
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
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
            {"Recommendation", "Replace arithmetic operations with bitwise operations where possible."},
            {"Example (Inefficient)", """
            <pre><code>
            int result = 1;
            for (int i = 0; i < iterations; i++) {
                result = result * 2;  // Inefficient multiplication by 2
                result = result / 2;  // Inefficient division by 2
            }
            </code></pre>
            """},
            {"Example (Optimized)", """
            <pre><code>
            int result = 1;
            for (int i = 0; i < iterations; i++) {
                result = result << 1;  // Efficient: Bitwise left shift (multiply by 2)
                result = result >> 1;  // Efficient: Bitwise right shift (divide by 2)
            }
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
