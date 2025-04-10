package org.msse696.automation;

import org.junit.jupiter.api.Test;
import org.msse696.optimization.helper.debug.Debug;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class ExpressionEliminationAnalyzerTest {

    private static final String INEFFICIENT_FILE_PATH = "src/test/java/org/msse696/automation/testfiles/ExpressionEliminationInefficient.java";
    private static final String EFFICIENT_FILE_PATH = "src/test/java/org/msse696/automation/testfiles/ExpressionEliminationEfficient.java";
    private ExpressionEliminationAnalyzer analyzer;

    @Test
    void testAnalyze_WithInefficientCode() {
        analyzer = new ExpressionEliminationAnalyzer();

        // Provide the inefficient file for analysis
        File inefficientFile = new File(INEFFICIENT_FILE_PATH);
        boolean optimizationNeeded = analyzer.analyze(inefficientFile, true);

        Debug.info("Optimization needed: " + optimizationNeeded);

        // Assert that inefficiencies are detected
        assertTrue(optimizationNeeded, "Optimization should be needed for redundant calculations.");

        // Assert that the report file is created
        File reportFile = new File(analyzer.getReport());
        assertTrue(reportFile.exists(), "HTML report should be created for redundant calculations.");
    }

    @Test
    void testAnalyze_WithEfficientCode() {
        analyzer = new ExpressionEliminationAnalyzer();

        // Provide the efficient file for analysis
        File efficientFile = new File(EFFICIENT_FILE_PATH);
        boolean optimizationNeeded = analyzer.analyze(efficientFile, true);

        Debug.info("Optimization needed: " + optimizationNeeded);

        // Assert that no inefficiencies are detected
        assertFalse(optimizationNeeded, "Optimization should NOT be needed for efficient code.");
    }
}
