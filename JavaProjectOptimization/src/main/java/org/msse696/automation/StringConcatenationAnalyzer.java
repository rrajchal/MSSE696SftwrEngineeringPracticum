package org.msse696.automation;

import org.msse696.optimization.helper.report.HtmlReport;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The StringConcatenationAnalyzer class analyzes methods in a given class
 * to detect inefficient string concatenation within loops and generates an HTML report if needed.
 */
public class StringConcatenationAnalyzer implements Analyzer {

    private static final String OUTPUT_REPORT = "target/results/reports/string_concatenation_report.html";

    // Simulated method code mappings for testing or analysis
    private final Map<String, String> simulatedCode = new HashMap<>();

    /**
     * Allows dynamic injection of simulated code for testing or analysis.
     *
     * @param methodName The name of the method.
     * @param code       The simulated code or comments for the method.
     */
    public void addSimulatedCode(String methodName, String code) {
        simulatedCode.put(methodName, code);
    }

    /**
     * Analyzes a class for inefficient string concatenation in loops.
     *
     * @param clazz The class to analyze.
     * @return True if inefficiencies are detected, false otherwise.
     */
    @Override
    public boolean analyze(Class<?> clazz) {
        System.out.println("Analyzing class: " + clazz.getName());

        Method[] methods = clazz.getDeclaredMethods();
        boolean inefficienciesFound = false;

        // Data for report generation
        List<String[]> inefficientMethods = new ArrayList<>();

        for (Method method : methods) {
            String methodName = method.getName();
            System.out.println("Analyzing method: " + methodName);

            String methodCode = getMethodCode(methodName);
            System.out.println("Extracted code or comments:\n" + methodCode);

            if (detectStringConcatenationInLoop(methodCode)) {
                inefficienciesFound = true;

                // Store method name and detected issue for the report
                inefficientMethods.add(new String[]{methodName, "String concatenation inside loop detected"});
                System.out.println("Issue detected in method: " + methodName);
            } else {
                System.out.println("No issues detected in method: " + methodName);
            }
        }

        if (inefficienciesFound) {
            System.out.println("\nInefficiencies detected. Creating report...");

            // Prepare data for the HTML report
            String[][] actualData = new String[inefficientMethods.size() + 1][2];
            actualData[0] = new String[]{"Method Name", "Issue"};
            for (int i = 0; i < inefficientMethods.size(); i++) {
                actualData[i + 1] = inefficientMethods.get(i);
            }

            // Call the function to get beautified recommended code examples
            String[][] recommendedData = generateRecommendedData();

            // Generate the report using the dedicated function
            generateReport(
                    "String Concatenation Analysis Report for " + clazz.getName(),
                    "Inefficient Methods Detected",
                    actualData,
                    "Best Practices with Example",
                    recommendedData,
                    OUTPUT_REPORT
            );
        } else {
            System.out.println("\nNo inefficiencies detected. Report will not be generated.");
        }

        return inefficienciesFound;
    }

    /**
     * Retrieves the simulated code for a method if available, or a placeholder message otherwise.
     *
     * @param methodName The name of the method to retrieve code for.
     * @return The simulated code or a default comment.
     */
    private String getMethodCode(String methodName) {
        return simulatedCode.getOrDefault(methodName, "// Unable to retrieve actual method body; consider adding static analysis tools for full functionality.");
    }

    /**
     * Generates the recommended data for the report.
     *
     * @return A 2D array containing the recommendation text and examples.
     */
    private String[][] generateRecommendedData() {
        return new String[][]{
                {"Recommendation", "Avoid string concatenation in loops. Use StringBuilder or StringBuffer instead."},
                {"Example (Inefficient)", """
                <pre><code>
                public String concatenateStrings(int iterations) {
                    String result = "";
                    for (int i = 0; i < iterations; i++) {
                        result += i;  // Inefficient: New String objects created in each iteration
                    }
                    return result;
                }
                </code></pre>
                """},
                {"Example (Optimized)", """
                <pre><code>
                public String concatenateStrings(int iterations) {
                    StringBuilder result = new StringBuilder();
                    for (int i = 0; i < iterations; i++) {
                        result.append(i);  // Efficient: StringBuilder reused across iterations
                    }
                    return result.toString();
                }
                </code></pre>
                """}
        };
    }

    /**
     * Generates an HTML report using the provided data.
     *
     * @param title            The title of the report.
     * @param actualHeader     The header for actual analysis data.
     * @param actualData       The actual analysis data.
     * @param recommendedHeader The header for recommended improvements.
     * @param recommendedData  The recommended improvement data.
     * @param outputPath       The file path where the report will be saved.
     */
    @Override
    public void generateReport(String title, String actualHeader, String[][] actualData, String recommendedHeader, String[][] recommendedData, String outputPath) {
        HtmlReport.generateHtmlReport(title, actualHeader, actualData, recommendedHeader, recommendedData, outputPath);
    }

    /**
     * Detects string concatenation inside loops.
     *
     * @param code The simulated code or comments to analyze.
     * @return True if inefficient string concatenation is detected, false otherwise.
     */
    private boolean detectStringConcatenationInLoop(String code) {
        System.out.println("Analyzing code for string concatenation patterns:\n" + code);

        // Match loops (for and while) including their bodies
        Pattern loopPattern = Pattern.compile("(for\\s*\\(.*?\\).*?\\{.*?\\}|while\\s*\\(.*?\\).*?\\{.*?\\})", Pattern.DOTALL);
        Matcher loopMatcher = loopPattern.matcher(code);

        // Match inefficient string concatenation patterns (`+=` and `result = result +`)
        Pattern inefficientConcatenationPattern = Pattern.compile("\\w+\\s*(\\+=|=\\s*\\w+\\s*\\+)\\s*.*?;");

        while (loopMatcher.find()) {
            String loopContent = loopMatcher.group();

            // Check for inefficient concatenation
            Matcher inefficientMatcher = inefficientConcatenationPattern.matcher(loopContent);
            if (inefficientMatcher.find()) {
                System.out.println("Inefficient string concatenation detected inside loop:\n" + inefficientMatcher.group());
                return true; // Inefficiency detected
            }
        }

        System.out.println("No string concatenation inefficiencies detected.");
        return false;
    }

    public String getReportName() {
        return OUTPUT_REPORT;
    }
}
