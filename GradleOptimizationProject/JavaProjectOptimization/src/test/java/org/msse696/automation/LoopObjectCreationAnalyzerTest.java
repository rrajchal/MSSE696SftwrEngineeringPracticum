package org.msse696.automation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;

public class LoopObjectCreationAnalyzerTest {
    private static final String INEFFICIENT_FILE_PATH = "src/test/java/org/msse696/automation/testfiles/LoopObjectCreationInefficient.java";
    private static final String EFFICIENT_FILE_PATH = "src/test/java/org/msse696/automation/testfiles/LoopObjectCreationEfficient.java";

    @Test
    void testAnalyze_WithInefficientCode() {
        LoopObjectCreationAnalyzer analyzer = new LoopObjectCreationAnalyzer();

        // Provide the actual .java file path to the analyzer
        File inefficientFile = new File(INEFFICIENT_FILE_PATH);
        boolean optimizationNeeded = analyzer.analyze(inefficientFile, true);

        System.out.println("Optimization needed: " + optimizationNeeded);

        // Assert that inefficiencies are detected
        Assertions.assertTrue(optimizationNeeded, "Optimization should be required for inefficient object creation in a loop.");

        // Assert that the report file is created
        File reportFile = new File(analyzer.getReport());
        Assertions.assertTrue(reportFile.exists(), "HTML report should be created for inefficient object creation in a loop.");
    }

    @Test
    void testAnalyze_WithEfficientCode() {
        LoopObjectCreationAnalyzer analyzer = new LoopObjectCreationAnalyzer();

        // Provide the actual .java file path to the analyzer
        File efficientFile = new File(EFFICIENT_FILE_PATH);
        boolean optimizationNeeded = analyzer.analyze(efficientFile, true);

        System.out.println("Optimization needed: " + optimizationNeeded);

        // Assert that no inefficiencies are detected
        Assertions.assertFalse(optimizationNeeded, "Optimization should NOT be required for efficient loop object creation.");
    }
}
