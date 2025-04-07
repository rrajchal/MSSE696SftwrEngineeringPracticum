package org.msse696.automation;

import org.msse696.optimization.helper.report.HtmlReport;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The LoopObjectCreationAnalyzer class analyzes methods in a given Java file
 * to detect inefficient object creation inside loops and generates an HTML report if optimization is needed.
 */
public class LoopObjectCreationAnalyzer implements Analyzer {

    private static final String OUTPUT_REPORT = "target/results/reports/loop_object_creation_report.html";
    private boolean isEfficient;

    /**
     * Analyzes a Java file to detect inefficient object creation inside loops.
     *
     * @param javaFile The Java file to analyze.
     * @return True if optimization is needed, false otherwise.
     */
    @Override
    public boolean analyze(File javaFile) {
        boolean optimizationNeeded = false;

        // Data for report generation
        List<String[]> inefficientMethods = new ArrayList<>();

        System.out.println("Analyzing file: " + javaFile.getName());

        try (FileInputStream fileInputStream = new FileInputStream(javaFile)) {
            // Simulate reading the file content
            String fileContent = new String(fileInputStream.readAllBytes());
            System.out.println("Parsed file content:\n" + fileContent);

            // Extract methods (simulating method analysis for simplicity)
            List<String> methods = simulateMethodExtraction(fileContent);

            for (String methodCode : methods) {
                if (detectObjectCreationInLoop(methodCode)) {
                    optimizationNeeded = true;

                    // Store the method name and detected issue for the report
                    String methodName = extractMethodName(methodCode);
                    inefficientMethods.add(new String[]{methodName, "Object creation inside loop detected"});
                    System.out.println("Issue detected in method: " + methodName);
                }
            }
        } catch (Exception e) {
            System.err.println("Error analyzing file: " + javaFile.getPath());
            e.printStackTrace();
        }

        if (optimizationNeeded) {
            System.out.println("\nOptimization is required. Creating report...");

            // Prepare data for the HTML report
            String[][] actualData = new String[inefficientMethods.size() + 1][2];
            actualData[0] = new String[]{"Method Name", "Issue"};
            for (int i = 0; i < inefficientMethods.size(); i++) {
                actualData[i + 1] = inefficientMethods.get(i);
            }

            String[][] recommendedData = getRecommendedData();

            // Generate the report using the dedicated function
            generateReport(
                    "Loop Object Creation Analysis Report for " + javaFile.getName(),
                    "Inefficient Methods Detected",
                    actualData,
                    "Best Practices with Example",
                    recommendedData,
                    OUTPUT_REPORT
            );
        } else {
            System.out.println("\nNo optimization required. Report will not be generated.");
        }
        isEfficient = !optimizationNeeded;
        return optimizationNeeded;
    }

    /**
     * Simulates method extraction from a file for analysis purposes.
     *
     * @param fileContent The content of the file.
     * @return A list of method codes as strings.
     */
    private List<String> simulateMethodExtraction(String fileContent) {
        // Simulate extracting methods using regex for simplicity
        List<String> methods = new ArrayList<>();
        Pattern methodPattern = Pattern.compile("(public|private|protected)\\s+.*?\\{.*?\\}", Pattern.DOTALL);
        Matcher methodMatcher = methodPattern.matcher(fileContent);

        while (methodMatcher.find()) {
            methods.add(methodMatcher.group());
        }

        return methods;
    }

    /**
     * Extracts the method name from the method code.
     *
     * @param methodCode The code of the method.
     * @return The method name as a string.
     */
    private String extractMethodName(String methodCode) {
        Pattern namePattern = Pattern.compile("(public|private|protected)\\s+\\w+\\s+(\\w+)\\s*\\(");
        Matcher nameMatcher = namePattern.matcher(methodCode);

        if (nameMatcher.find()) {
            return nameMatcher.group(2); // Extracts the method name
        }

        return "Unknown Method";
    }

    /**
     * Detects object creation inside loops.
     *
     * @param code The simulated code to analyze.
     * @return True if inefficient object creation inside loops is detected, false otherwise.
     */
    private boolean detectObjectCreationInLoop(String code) {
        System.out.println("Analyzing code for loop and object creation patterns:\n" + code);

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

        System.out.println("No inefficient object creation detected.");
        return false;
    }

    @Override
    public String[][] getRecommendedData() {
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

    @Override
    public String getReport() {
        return OUTPUT_REPORT;
    }

    @Override
    public boolean isEfficient() {
        return isEfficient;
    }
}
