package org.msse696.automation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.io.File;

/**
 * Unit tests for the LoopInefficiencyAnalyzer class.
 */
class LoopInefficiencyAnalyzerTest {

    private static final String INEFFICIENT_FILE_PATH = "src/test/java/org/msse696/automation/testfiles/LoopInefficient.java";
    private static final String EFFICIENT_FILE_PATH = "src/test/java/org/msse696/automation/testfiles/LoopEfficient.java";

    private LoopInefficiencyAnalyzer analyzer;

    @BeforeEach
    void setup() {
        analyzer = new LoopInefficiencyAnalyzer();
    }

    @Test
    void testAnalyze_WithInefficientCode() {
        File inefficientFile = new File(INEFFICIENT_FILE_PATH);

        // Analyze inefficient code
        boolean inefficienciesDetected = analyzer.analyze(inefficientFile, true);
        Assertions.assertTrue(inefficienciesDetected, "Inefficiencies should be detected for repeated method calls in loop conditions or body.");

        // Verify that the report is created
        File reportFile = new File(analyzer.getReport());
        Assertions.assertTrue(reportFile.exists(), "HTML report should be created for inefficient loops.");
    }

    @Test
    void testAnalyze_WithEfficientCode() {
        File efficientFile = new File(EFFICIENT_FILE_PATH);

        // Analyze efficient code
        boolean inefficienciesDetected = analyzer.analyze(efficientFile, true);
        Assertions.assertFalse(inefficienciesDetected, "No inefficiencies should be detected for precomputed loop limits.");
    }
}
