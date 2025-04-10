package org.msse696.automation;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.CastExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.type.Type;
import org.msse696.optimization.helper.debug.Debug;
import org.msse696.optimization.helper.report.HtmlReport;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * The CastingAnalyzer class detects inefficient type casting practices
 * where explicit casting is used instead of direct assignment.
 */
public class CastingAnalyzer implements Analyzer {

    private static final String OUTPUT_REPORT = "target/results/reports/casting_analysis_report.html";
    private boolean isEfficient;

    @Override
    public boolean analyze(File javaFile, boolean createReort) {
        Debug.info("Analyzing file: " + javaFile.getName());
        List<String[]> inefficiencies = new ArrayList<>();
        boolean inefficiencyDetected = false;

        try (FileInputStream fileInputStream = new FileInputStream(javaFile)) {
            // Parse the Java file into a CompilationUnit
            CompilationUnit compilationUnit = StaticJavaParser.parse(fileInputStream);
            Debug.info("Parsed CompilationUnit:\n" + compilationUnit);
            // Analyze methods within the file
            for (MethodDeclaration method : compilationUnit.findAll(MethodDeclaration.class)) {
                Debug.info("Analyzing method: " + method.getName());
                boolean methodHasInefficiency = detectInefficientCasting(method);
                if (methodHasInefficiency) {
                    inefficiencyDetected = true;
                    inefficiencies.add(new String[]{method.getNameAsString(), "Explicit casting detected where direct assignment is possible"});
                }
            }

        } catch (Exception e) {
            System.err.println("Error analyzing file: " + javaFile.getPath());
        }

        // Generate a report if inefficiencies are detected
        if (inefficiencyDetected && createReort) {
            Debug.info("\nInefficiencies detected. Generating report...");
            generateReport(
                "Casting Analysis Report",
                "Methods with Inefficient Casting",
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
     * Detects inefficient explicit casting practices in the method.
     *
     * @param method The method to analyze.
     * @return True if inefficiencies are detected, false otherwise.
     */
    private boolean detectInefficientCasting(MethodDeclaration method) {
        List<CastExpr> castExpressions = method.findAll(CastExpr.class);
        AtomicBoolean inefficiencyDetected = new AtomicBoolean(false);
        for (CastExpr castExpr : castExpressions) {
            Type castType = castExpr.getType();
            String castTypeName = castType.toString();
            Expression castedExpression = castExpr.getExpression();

            // If the cast expression is a simple NameExpr (a variable)
            if (castedExpression.isNameExpr()) {
                String variableName = castedExpression.asNameExpr().getNameAsString();

                // Find the declaration of this variable within the current method or its scope
                method.findAll(VariableDeclarationExpr.class).forEach(varDeclExpr -> {
                    varDeclExpr.getVariables().forEach(variableDeclarator -> {
                        if (variableDeclarator.getNameAsString().equals(variableName)) {
                            Type variableType = variableDeclarator.getType();
                            String variableTypeName = variableType.toString();

                            // Check for inefficient casting: primitive to compatible wrapper
                            if (isPrimitiveWrapperPair(variableTypeName, castTypeName)) {
                                Debug.info("Inefficient explicit casting detected for: " + castExpr);
                                inefficiencyDetected.set(true); // Set the class-level flag
                            }
                        }
                    });
                });
            }
        }
        return inefficiencyDetected.get();
    }

    /**
     * Checks if a given primitive type and its corresponding wrapper type form a valid primitive-wrapper pair.
     *
     * @param primitiveType The name of the primitive type (e.g., "int", "float", "char").
     * @param wrapperType The name of the wrapper type (e.g., "Integer", "Float", "Character").
     * @return {@code true} if the provided types form a valid primitive-wrapper pair, {@code false} otherwise.
     */
     private boolean isPrimitiveWrapperPair(String primitiveType, String wrapperType) {
        return (primitiveType.equals("int") && wrapperType.equals("Integer")) ||
                (primitiveType.equals("byte") && wrapperType.equals("Byte")) ||
                (primitiveType.equals("short") && wrapperType.equals("Short")) ||
                (primitiveType.equals("long") && wrapperType.equals("Long")) ||
                (primitiveType.equals("float") && wrapperType.equals("Float")) ||
                (primitiveType.equals("double") && wrapperType.equals("Double")) ||
                (primitiveType.equals("boolean") && wrapperType.equals("Boolean")) ||
                (primitiveType.equals("char") && wrapperType.equals("Character"));
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
            public void execute(int iterations) {
                for (int i = 0; i < iterations; i++) {
                    Integer assignedValue = (Integer) i; // Explicit casting
                    int total = assignedValue + assignedValue;
                }
            }
            </code></pre>
            """},
            {"Efficient Code", """
            <pre><code>
            public void execute(int iterations) {
                for (int i = 0; i < iterations; i++) {
                    Integer assignedValue = i; // Direct assignment
                    int total = assignedValue + assignedValue;
                }
            }
            </code></pre>
            """}
        };
    }
}
