package org.msse696.automation;

import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import org.msse696.optimization.helper.debug.Debug;
import org.msse696.optimization.helper.report.HtmlReport;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class StringConcatenationAnalyzer implements Analyzer {

    private static final String OUTPUT_REPORT = "target/results/reports/string_concatenation_report.html";
    private boolean isEfficient;

    public StringConcatenationAnalyzer() {
        CombinedTypeSolver typeSolver = new CombinedTypeSolver();
        typeSolver.add(new ReflectionTypeSolver());
        JavaSymbolSolver symbolSolver = new JavaSymbolSolver(typeSolver);
        ParserConfiguration parserConfiguration = new ParserConfiguration();
        parserConfiguration.setSymbolResolver(symbolSolver);
    }

    @Override
    public boolean analyze(File javaFile, boolean createReport) {
        AtomicBoolean optimizationNeeded = new AtomicBoolean(false);
        List<String[]> inefficientMethods = new ArrayList<>();

        try (FileInputStream fileInputStream = new FileInputStream(javaFile)) {
            CompilationUnit compilationUnit = StaticJavaParser.parse(fileInputStream);
            Debug.info("Parsed CompilationUnit:\n" + compilationUnit.toString());

            compilationUnit.findAll(MethodDeclaration.class).forEach(method -> {
                Debug.info("Analyzing method: " + method.getName());
                if (detectStringConcatenationInLoop(method)) {
                    optimizationNeeded.set(true);
                    inefficientMethods.add(new String[]{method.getNameAsString(), "String concatenation inside loop detected"});
                    Debug.info("Issue detected in method: " + method.getName());
                } else {
                    Debug.info("No issues detected in method: " + method.getName());
                }
            });

        } catch (Exception e) {
            Debug.error("Error analyzing Java file: " + javaFile.getPath());
        }

        if (optimizationNeeded.get() && createReport) {
            generateReport(
                    "String Concatenation Analysis Report",
                    "Methods with Inefficient String Concatenation",
                    prepareReportData(inefficientMethods),
                    "Recommendations for Optimization",
                    getRecommendedData(),
                    OUTPUT_REPORT
            );
        } else {
            Debug.info("No optimization required. Report will not be generated.");
        }
        isEfficient = !optimizationNeeded.get();
        return optimizationNeeded.get();
    }

    private boolean detectStringConcatenationInLoop(MethodDeclaration method) {
        Debug.info("Analyzing method: " + method.getName());
        List<ForStmt> loops = method.findAll(ForStmt.class);

        for (ForStmt loop : loops) {
            Debug.info("Analyzing loop body: " + loop.getBody());
            if (loop.getBody().isBlockStmt()) {
                for (Statement statement : loop.getBody().asBlockStmt().getStatements()) {
                    Debug.info("Statement found: " + statement);
                    if (checkForConcatenation(statement)) {
                        return true;
                    }
                }
            } else if (checkForConcatenation(loop.getBody())) {
                return true;
            }
        }
        return false;
    }

    private boolean checkForConcatenation(Statement statement) {
        // Check if the statement is an assignment expression (`result += i`)
        if (statement.isExpressionStmt() && statement.asExpressionStmt().getExpression() instanceof AssignExpr) {
            AssignExpr assignExpr = statement.asExpressionStmt().getExpression().asAssignExpr();
            Debug.info("Analyzing Assignment Expression: " + assignExpr);

            // Check if the operator is PLUS_ASSIGN (+=)
            if (assignExpr.getOperator() == AssignExpr.Operator.PLUS) {
                if (assignExpr.getTarget() instanceof NameExpr) {
                    NameExpr target = assignExpr.getTarget().asNameExpr();
                    String variableName = target.getNameAsString();

                    // TEMPORARY ASSUMPTION: Assume "result" is a String
                    if ("result".equals(variableName)) {
                        Debug.info("Inefficient string concatenation detected: " + assignExpr);
                        return true; // Flag as inefficient concatenation
                    }
                }
            }
        }

        // Detect efficient StringBuilder operations (`result.append(...)`)
        if (statement.isExpressionStmt() && statement.asExpressionStmt().getExpression() instanceof MethodCallExpr) {
            MethodCallExpr methodCallExpr = statement.asExpressionStmt().getExpression().asMethodCallExpr();
            if ("append".equals(methodCallExpr.getNameAsString())) {
                Debug.info("Efficient string concatenation detected (StringBuilder): " + methodCallExpr);
            }
        }

        return false; // No issues detected
    }


    private String[][] prepareReportData(List<String[]> inefficientMethods) {
        String[][] data = new String[inefficientMethods.size() + 1][2];
        data[0] = new String[]{"Method Name", "Issue"};
        for (int i = 0; i < inefficientMethods.size(); i++) {
            data[i + 1] = inefficientMethods.get(i);
        }
        return data;
    }

    @Override
    public String[][] getRecommendedData() {
        return new String[][]{
                {"Recommendation", "Avoid string concatenation in loops. Use StringBuilder or StringBuffer instead."},
                {"Example (Inefficient)", """
            public String concatenateStrings(int iterations) {
                String result = "";
                for (int i = 0; i < iterations; i++) {
                    result += i;  // Inefficient: New String objects created in each iteration
                }
                return result;
            }
            """},
                {"Example (Optimized)", """
            public String concatenateStrings(int iterations) {
                StringBuilder result = new StringBuilder();
                for (int i = 0; i < iterations; i++) {
                    result.append(i);  // Efficient: StringBuilder reused across iterations
                }
                return result.toString();
            }
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

    @Override
    public boolean isEfficient() {
        return isEfficient;
    }
}
