package org.msse696.automation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.io.File;

/**
 * Unit tests for the InstanceofAnalyzer class.
 */
class InstanceofAnalyzerTest {

    private static final String INEFFICIENT_FILE_PATH = "src/test/java/org/msse696/automation/testfiles/InstanceofInefficient.java";
    private static final String EFFICIENT_FILE_PATH = "src/test/java/org/msse696/automation/testfiles/InstanceofEfficient.java";

    private InstanceofAnalyzer analyzer;

    @BeforeEach
    void setup() {
        analyzer = new InstanceofAnalyzer();
    }

    @Test
    void testAnalyze_WithInefficientCode() {
        File inefficientFile = new File(INEFFICIENT_FILE_PATH);

        // Analyze inefficient code
        boolean inefficienciesDetected = analyzer.analyze(inefficientFile, true);
        Assertions.assertTrue(inefficienciesDetected, "Inefficiencies should be detected for type validation using try-catch blocks.");

        // Verify that the report is created
        File reportFile = new File(analyzer.getReport());
        Assertions.assertTrue(reportFile.exists(), "HTML report should be created for inefficient type validation.");
    }

    @Test
    void testAnalyze_WithEfficientCode() {
        File efficientFile = new File(EFFICIENT_FILE_PATH);

        // Analyze efficient code
        boolean inefficienciesDetected = analyzer.analyze(efficientFile, true);
        Assertions.assertFalse(inefficienciesDetected, "No inefficiencies should be detected for type validation using instanceof.");
    }
}
