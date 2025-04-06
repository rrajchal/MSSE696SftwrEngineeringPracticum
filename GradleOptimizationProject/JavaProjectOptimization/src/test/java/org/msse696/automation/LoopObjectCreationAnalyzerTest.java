package org.msse696.automation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

public class LoopObjectCreationAnalyzerTest {
    private static final String INEFFICIENT_FILE_PATH = "src/test/java/org/msse696/automation/testfiles/LoopObjectCreationInefficient.java";
    private static final String EFFICIENT_FILE_PATH = "src/test/java/org/msse696/automation/testfiles/LoopObjectCreationEfficient.java";

    @BeforeEach
    void setup() {
        // Ensure the report file is deleted before every test
        File reportFile = new File("target/results/reports/loop_object_creation_report.html");
        if (reportFile.exists()) {
            reportFile.delete();
        }
    }

    @Test
    void testAnalyze_WithInefficientCode() {
        LoopObjectCreationAnalyzer analyzer = new LoopObjectCreationAnalyzer();

        // Provide the actual .java file path to the analyzer
        File inefficientFile = new File(INEFFICIENT_FILE_PATH);
        boolean optimizationNeeded = analyzer.analyze(inefficientFile);

        System.out.println("Optimization needed: " + optimizationNeeded);

        // Assert that inefficiencies are detected
        Assertions.assertTrue(optimizationNeeded, "Optimization should be required for inefficient object creation in a loop.");

        // Assert that the report file is created
        File reportFile = new File(analyzer.getReportName());
        Assertions.assertTrue(reportFile.exists(), "HTML report should be created for inefficient object creation in a loop.");
    }

    @Test
    void testAnalyze_WithEfficientCode() {
        LoopObjectCreationAnalyzer analyzer = new LoopObjectCreationAnalyzer();

        // Provide the actual .java file path to the analyzer
        File efficientFile = new File(EFFICIENT_FILE_PATH);
        boolean optimizationNeeded = analyzer.analyze(efficientFile);

        System.out.println("Optimization needed: " + optimizationNeeded);

        // Assert that no inefficiencies are detected
        Assertions.assertFalse(optimizationNeeded, "Optimization should NOT be required for efficient loop object creation.");

        // Assert that the report file is NOT created
        File reportFile = new File(analyzer.getReportName());
        Assertions.assertFalse(reportFile.exists(), "HTML report should NOT be created for efficient loop object creation.");
    }
}
