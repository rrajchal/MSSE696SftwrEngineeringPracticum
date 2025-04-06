package org.msse696.automation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

public class StringConcatenationAnalyzerTest {

    @BeforeEach
    void setup() {
        File reportFile = new File("target/results/reports/string_concatenation_report.html");
        if (reportFile.exists()) {
            reportFile.delete();
        }
    }

    // Test class with inefficient string concatenation in a loop
    static class StringConcatenationInefficient {
        public String concatenateStrings(int iterations) {
            String result = "";
            for (int i = 0; i < iterations; i++) {
                result += i; // Inefficient string concatenation
            }
            return result;
        }
    }

    // Test class with efficient string manipulation using StringBuilder
    static class StringConcatenationEfficient {
        public String concatenateStrings(int iterations) {
            StringBuilder result = new StringBuilder();
            for (int i = 0; i < iterations; i++) {
                result.append(i); // Efficient string concatenation
            }
            return result.toString();
        }
    }

    @Test
    void testAnalyze_WithInefficientCode() {
        StringConcatenationAnalyzer analyzer = new StringConcatenationAnalyzer();

        // Inject simulated code for the method "concatenateStrings"
        analyzer.addSimulatedCode("concatenateStrings", """
        public String concatenateStrings(int iterations) {
            String result = "";
            for (int i = 0; i < iterations; i++) {
                result += i; // Inefficient string concatenation
            }
            return result;
        }
    """);

        boolean inefficienciesDetected = analyzer.analyze(StringConcatenationInefficient.class);
        System.out.println("Inefficiencies detected: " + inefficienciesDetected);

        // Check that inefficiencies are detected
        Assertions.assertTrue(inefficienciesDetected, "Inefficiencies should be detected in the code.");

        // Check that the HTML report is created
        File reportFile = new File(analyzer.getReportName());
        Assertions.assertTrue(reportFile.exists(), "HTML report should be created for inefficient string concatenation.");
    }

    @Test
    void testAnalyze_WithEfficientCode() {
        StringConcatenationAnalyzer analyzer = new StringConcatenationAnalyzer();

        // Inject simulated code for the method
        analyzer.addSimulatedCode("concatenateStrings", """
        public String concatenateStrings(int iterations) {
            StringBuilder result = new StringBuilder();
            for (int i = 0; i < iterations; i++) {
                result.append(i); // Efficient string concatenation
            }
            return result.toString();
        }
    """);

        File reportFile = new File(analyzer.getReportName());

        boolean inefficienciesDetected = analyzer.analyze(StringConcatenationEfficient.class);
        System.out.println("Inefficiencies detected: " + inefficienciesDetected);

        // Check that no inefficiencies are detected
        Assertions.assertFalse(inefficienciesDetected, "No inefficiencies should be detected in the code.");

        // Verify that the HTML report file does not exist
        Assertions.assertFalse(reportFile.exists(), "HTML report should NOT be created for efficient string manipulation.");
    }



    @Test
    void testAnalyze_WithEfficientCode1() {
        StringConcatenationAnalyzer analyzer = new StringConcatenationAnalyzer();
        boolean inefficienciesDetected = analyzer.analyze(StringConcatenationEfficient.class);

        System.out.println("Inefficiencies detected: " + inefficienciesDetected);

        // Check that no inefficiencies are detected
        Assertions.assertFalse(inefficienciesDetected, "No inefficiencies should be detected in the code.");

        // Check that the HTML report is not created
        File reportFile = new File(analyzer.getReportName());
        Assertions.assertFalse(reportFile.exists(), "HTML report should NOT be created for efficient string manipulation.");
    }

    @Test
    void testAnalyze_ReportFileGenerated() {

        StringConcatenationAnalyzer analyzer = new StringConcatenationAnalyzer();
        boolean inefficienciesDetected = analyzer.analyze(StringConcatenationInefficient.class);

        System.out.println("Inefficiencies detected: " + inefficienciesDetected);

        // Verify that the report file is created
        File reportFile = new File(analyzer.getReportName());
        Assertions.assertTrue(reportFile.exists(), "HTML report should be created when inefficiencies are detected.");
    }

    @Test
    void testAnalyze_WithInefficientCode1() {
        StringConcatenationAnalyzer analyzer = new StringConcatenationAnalyzer();

        boolean inefficienciesDetected = analyzer.analyze(StringConcatenationInefficient.class);
        System.out.println("Inefficiencies detected: " + inefficienciesDetected);

        // Check that inefficiencies are detected
        Assertions.assertTrue(inefficienciesDetected, "Inefficiencies should be detected in the code.");

        // Check that the HTML report is created
        File reportFile = new File(analyzer.getReportName());
        Assertions.assertTrue(reportFile.exists(), "HTML report should be created for inefficient string concatenation.");
    }
}
