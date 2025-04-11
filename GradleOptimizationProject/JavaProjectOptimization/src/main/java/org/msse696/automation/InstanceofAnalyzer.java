package org.msse696.automation;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.InstanceOfExpr;
import com.github.javaparser.ast.stmt.TryStmt;
import org.msse696.optimization.helper.debug.Debug;
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

     @Override
    public boolean analyze(File javaFile, boolean createReport) {
        Debug.info("Analyzing file: " + javaFile.getName());
        List<String[]> inefficiencies = new ArrayList<>(); // Tracks inefficiencies for report generation
        boolean inefficiencyDetected = false;

        try (FileInputStream fileInputStream = new FileInputStream(javaFile)) {
            // Parse the Java file into a CompilationUnit
            CompilationUnit compilationUnit = StaticJavaParser.parse(fileInputStream);
            Debug.info("Parsed CompilationUnit:\n" + compilationUnit);

            // Analyze methods within the file
            for (MethodDeclaration method : compilationUnit.findAll(MethodDeclaration.class)) {
                Debug.info("Analyzing method: " + method.getName());
                boolean methodHasInefficiency = detectInefficientTypeValidation(method);
                if (methodHasInefficiency) {
                    inefficiencyDetected = true;
                    inefficiencies.add(new String[]{method.getNameAsString(), "Inefficient type validation detected"});
                }
            }

        } catch (Exception e) {
            Debug.error("Error analyzing file: " + javaFile.getPath());
        }

        // Only generate a report if inefficiencies are detected
        if (inefficiencyDetected && createReport) {
            Debug.info("\nInefficiencies detected. Generating report...");
            generateReport(
                "Type Validation Analysis Report",
                "Methods with Inefficient Type Validation",
                prepareReportData(inefficiencies),
                "Recommendations for Optimization",
                getRecommendedData(),
                OUTPUT_REPORT
            );
        } else {
            Debug.info("\nNo inefficiencies detected. Report will not be generated.");
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
                Debug.info("Inefficient type validation detected inside try-catch block.");
                return true;
            }
        }

        // Ensure instanceof is properly used for type checking in the method
        List<InstanceOfExpr> instanceofExpressions = method.findAll(InstanceOfExpr.class);
        if (!instanceofExpressions.isEmpty()) {
            Debug.info("Efficient type validation detected using instanceof.");
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
            {"Example", "Code"},
            {"Inefficient Code", """
            public void execute(int iterations) {
                Object obj = "123"; // Example object as a String representing a number
                for (int i = 0; i < iterations; i++) {
                    try {
                        performOperation(obj);  // Tries to convert Object to String
                    } catch (ClassCastException | NumberFormatException e) {
                        Debug.error("Invalid cast or format: " + e.getMessage());
                    }
                }
            }
            """},
            {"Efficient Code", """
            public void execute(int iterations) {
                Object obj = "123"; // Example object as a String representing a number
                for (int i = 0; i < iterations; i++) {
                    if (obj instanceof String) {
                        performOperation(obj);  // Converts Object to String directly without try-catch
                    }
                }
            }
            """}
        };
    }
}
