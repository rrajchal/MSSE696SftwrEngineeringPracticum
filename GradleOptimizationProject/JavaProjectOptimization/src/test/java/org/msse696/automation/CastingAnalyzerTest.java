package org.msse696.automation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.io.File;

/**
 * Unit tests for the CastingAnalyzer class.
 */
class CastingAnalyzerTest {

    private static final String INEFFICIENT_FILE_PATH = "src/test/java/org/msse696/automation/testfiles/CastingInefficient.java";
    private static final String EFFICIENT_FILE_PATH = "src/test/java/org/msse696/automation/testfiles/CastingEfficient.java";

    private CastingAnalyzer analyzer;

    @BeforeEach
    void setup() {
        analyzer = new CastingAnalyzer();
    }

    @Test
    void testAnalyze_WithInefficientCode() {
        File inefficientFile = new File(INEFFICIENT_FILE_PATH);

        // Analyze inefficient code
        boolean inefficienciesDetected = analyzer.analyze(inefficientFile);
        Assertions.assertTrue(inefficienciesDetected, "Inefficiencies should be detected for explicit casting where direct assignment is possible.");

        // Verify that the report is created
        File reportFile = new File(analyzer.getReport());
        Assertions.assertTrue(reportFile.exists(), "HTML report should be created for inefficient casting.");
    }

    @Test
    void testAnalyze_WithEfficientCode() {
        File efficientFile = new File(EFFICIENT_FILE_PATH);

        // Analyze efficient code
        boolean inefficienciesDetected = analyzer.analyze(efficientFile);
        Assertions.assertFalse(inefficienciesDetected, "No inefficiencies should be detected for direct assignment.");
    }
}
