package org.msse696.automation;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.InstanceOfExpr;
import com.github.javaparser.ast.stmt.TryStmt;
import org.msse696.optimization.helper.report.HtmlReport;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * The InstanceofAnalyzer class detects inefficient type validation practices
 * in Java files, specifically using try-catch blocks instead of instanceof.
 */
public class InstanceofAnalyzer implements Analyzer {

    private static final String OUTPUT_REPORT = "target/results/reports/instanceof_analysis_report.html";
    private boolean isEfficient;

    /**
     * Analyzes the given Java file for inefficient type validation practices.
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
                boolean methodHasInefficiency = detectInefficientTypeValidation(method);
                if (methodHasInefficiency) {
                    inefficiencyDetected = true;
                    inefficiencies.add(new String[]{method.getNameAsString(), "Inefficient type validation detected"});
                }
            }

        } catch (Exception e) {
            System.err.println("Error analyzing file: " + javaFile.getPath());
        }

        // Only generate a report if inefficiencies are detected
        if (inefficiencyDetected) {
            System.out.println("\nInefficiencies detected. Generating report...");
            generateReport(
                "Type Validation Analysis Report",
                "Methods with Inefficient Type Validation",
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
     * Detects inefficient type validation within a method's code.
     *
     * @param method The method to analyze.
     * @return True if inefficiencies are detected, false otherwise.
     */
    private boolean detectInefficientTypeValidation(MethodDeclaration method) {
        // Check for try-catch blocks containing logic related to type validation
        List<TryStmt> tryStmts = method.findAll(TryStmt.class);
        for (TryStmt tryStmt : tryStmts) {
            if (tryStmt.findFirst(BinaryExpr.class, expr -> expr.getOperator().name().contains("InstanceOf")).isEmpty()) {
                System.out.println("Inefficient type validation detected inside try-catch block.");
                return true;
            }
        }

        // Ensure instanceof is properly used for type checking in the method
        List<InstanceOfExpr> instanceofExpressions = method.findAll(InstanceOfExpr.class);
        if (!instanceofExpressions.isEmpty()) {
            System.out.println("Efficient type validation detected using instanceof.");
        }
        return false;
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
            {"Inefficient Code", """
            <pre><code>
            public void execute(int iterations) {
                Object obj = "123"; // Example object as a String representing a number
                for (int i = 0; i < iterations; i++) {
                    try {
                        performOperation(obj);  // Tries to convert Object to String
                    } catch (ClassCastException | NumberFormatException e) {
                        System.err.println("Invalid cast or format: " + e.getMessage());
                    }
                }
            }
            </code></pre>
            """},
            {"Efficient Code", """
            <pre><code>
            public void execute(int iterations) {
                Object obj = "123"; // Example object as a String representing a number
                for (int i = 0; i < iterations; i++) {
                    if (obj instanceof String) {
                        performOperation(obj);  // Converts Object to String directly without try-catch
                    }
                }
            }
            </code></pre>
            """}
        };
    }
}
