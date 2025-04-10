package org.msse696.automation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.msse696.optimization.helper.debug.Debug;

import java.io.File;

/**
 * Unit tests for the CastingAnalyzer class.
 */
class CombinedTest {

    private static final String FILE_PATH = "src/test/java/org/msse696/automation/testfiles/CombinedTestClass.java";

    private InefficiencyAnalyzerRunner analyzer;

    @BeforeEach
    void setup() {
        analyzer = new InefficiencyAnalyzerRunner();
    }

    @Test
    void testAnalyze_WithInefficientCode() {
        File inefficientFile = new File(FILE_PATH);

        // Analyze inefficient code
        boolean inefficienciesDetected = analyzer.analyze(inefficientFile, true);
        Assertions.assertTrue(inefficienciesDetected, "Inefficiencies should be detected for explicit casting where direct assignment is possible.");

        // Verify that the report is created
        File reportFile = new File(analyzer.getReport());
        Assertions.assertTrue(reportFile.exists(), "HTML report should be created for inefficient casting.");
    }

    @Test
    void testCountInefficiencyCount() {
        File inefficientFile = new File(FILE_PATH);

        // Analyze inefficient code
        analyzer.analyze(inefficientFile, false);

        int totalInefficiencyCount = analyzer.getTotalInefficiencyCount();

        Assertions.assertTrue(totalInefficiencyCount > 0);
        Debug.info("Total Inefficiencies: " + totalInefficiencyCount);

    }
}
