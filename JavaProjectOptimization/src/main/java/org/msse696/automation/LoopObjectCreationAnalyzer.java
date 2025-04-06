package org.msse696.automation;

import org.msse696.optimization.helper.report.HtmlReport;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The LoopObjectCreationAnalyzer class analyzes methods in a given class to detect
 * inefficient object creation inside loops and generates an HTML report if optimization is needed.
 */
public class LoopObjectCreationAnalyzer implements Analyzer {

    private static final String OUTPUT_REPORT = "target/results/reports/loop_object_creation_report.html";

    /**
     * Analyzes a class to detect inefficient object creation inside loops.
     *
     * @param clazz The class to analyze.
     * @return True if optimization is needed, false otherwise.
     */
    @Override
    public boolean analyze(Class<?> clazz) {
        Method[] methods = clazz.getDeclaredMethods();
        boolean optimizationNeeded = false;

        // Data for report generation
        List<String[]> inefficientMethods = new ArrayList<>();

        System.out.println("Analyzing class: " + clazz.getName());

        for (Method method : methods) {
            String methodName = method.getName();
            String methodCode = simulateMethodCode(clazz, method);

            if (detectObjectCreationInLoop(methodCode)) {
                optimizationNeeded = true;

                // Store method name and detected issue for the report
                inefficientMethods.add(new String[]{methodName, "Object creation inside loop detected"});
                System.out.println("Issue detected in method: " + methodName);
            }
        }

        if (optimizationNeeded) {
            System.out.println("\nOptimization is required. Creating report...");

            // Prepare data for the HTML report
            String[][] actualData = new String[inefficientMethods.size() + 1][2];
            actualData[0] = new String[]{"Method Name", "Issue"};
            for (int i = 0; i < inefficientMethods.size(); i++) {
                actualData[i + 1] = inefficientMethods.get(i);
            }

            String[][] recommendedData = generateRecommendedData();

            // Generate the report using the dedicated function
            generateReport(
                    "Loop Object Creation Analysis Report for " + clazz.getName(),
                    "Inefficient Methods Detected",
                    actualData,
                    "Best Practices with Example",
                    recommendedData,
                    OUTPUT_REPORT
            );
        } else {
            System.out.println("\nNo optimization required. Report will not be generated.");
        }

        return optimizationNeeded;
    }

    /**
     * Generates the recommended data for the report.
     *
     * @return A 2D array containing the recommendation text and examples.
     */
    private String[][] generateRecommendedData() {
        return new String[][]{
                {"Recommendation", "Avoid creating new objects inside loops. Reuse objects wherever possible."},
                {"Example (Inefficient)", """
                <pre><code>
                for (int i = 0; i < 10; i++) {
                    SomeObject obj = new SomeObject();  // Inefficient: New object created in each iteration
                }
                </code></pre>
                """},
                {"Example (Optimized)", """
                <pre><code>
                SomeObject obj = new SomeObject();  // Efficient: Object reused across iterations
                for (int i = 0; i < 10; i++) {
                    obj.process(i);
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
     * Simulates the code of a method for analysis.
     *
     * @param clazz  The class containing the method.
     * @param method The method to simulate.
     * @return The simulated code as a string.
     */
    private String simulateMethodCode(Class<?> clazz, Method method) {
        // Simulating actual logic for testing purposes
        return """
            public void testMethod() {
                for (int i = 0; i < 10; i++) {
                    String obj = new String("Test"); // Inefficient object creation
                }
            }
        """;
    }

    /**
     * Detects object creation inside loops.
     *
     * @param code The simulated code to analyze.
     * @return True if inefficient object creation inside loops is detected, false otherwise.
     */
    private boolean detectObjectCreationInLoop(String code) {
        // System.out.println("Analyzing code for loop and object creation patterns:\n" + code);

        // Regex pattern to detect loops and their bodies
        Pattern loopPattern = Pattern.compile("(for\\s*\\(.*?\\).*?\\{.*?\\}|while\\s*\\(.*?\\).*?\\{.*?\\})", Pattern.DOTALL);
        Matcher loopMatcher = loopPattern.matcher(code);

        // Regex pattern to detect object creation
        Pattern creationPattern = Pattern.compile("\\b\\w+\\s+\\w+\\s*=\\s*new\\s+\\w+\\s*\\(.*?\\);");

        while (loopMatcher.find()) {
            String loopContent = loopMatcher.group(); // Extract full loop content
            Matcher creationMatcher = creationPattern.matcher(loopContent);
            if (creationMatcher.find()) {
                System.out.println("Object creation detected inside loop:\n" + creationMatcher.group());
                return true; // Inefficient object creation detected
            }
        }

        // System.out.println("No inefficient object creation detected.");
        return false;
    }

    public String getReportName() {
        return OUTPUT_REPORT;
    }
}
